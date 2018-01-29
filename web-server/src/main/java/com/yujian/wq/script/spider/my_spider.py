# https://www.dbmeinv.com/dbgroup/current.htm?cid=0

from urllib.request import urlopen
import urllib.request

import requests
from bs4 import BeautifulSoup
import os, sys, time
import http.cookiejar
import uuid
import pymysql
import hashlib
import os
from selenium import webdriver
from selenium.webdriver import DesiredCapabilities
from selenium.webdriver.common.keys import Keys
import requests
from bs4 import BeautifulSoup
import lxml
from PIL import Image
import os
import ssl

# ---------------------全局配置区开始---------------------
path = 'I:/doc/deeplearn/img2/'
# path = '/Users/wangqing/Documents/code/wechat/img/'
# path = '/Users/wangqing/Documents/code/my/data/deeplearn/img/'

dbmv_url = "http://www.dbmeinv.com"
jiandan_url = "http://jandan.net"
phantomjs = r'I:\program\phantomjs-2.1.1-windows\bin\phantomjs.exe'
# phantomjs = r'/Users/wangqing/Documents/program/phantomjs-2.1.1-macosx/bin/phantomjs'

# ---------------------全局配置区结束---------------------
ssl._create_default_https_context = ssl._create_unverified_context


# ---------------------图片处理区开始---------------------
def handle_img(arry):
    conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='yujian', charset="utf8")
    isOk = True
    md5str =''
    for data in arry:
        md5str = data['md5']
        if has_md5(conn, md5str):
            isOk = False
            break
    print('是否允许写入：' + str(isOk) + "；md5:" + str(md5str))
    if isOk:
        for data in arry:
            write_db(conn, data['name'], data['title'], data['chain'], data['md5'])
    else:
        for data in arry:
            os.remove(data['path'])
    conn.close()


def write_db(conn, img, title, chain, md5):
    cur = conn.cursor()
    try:
        cur.execute("insert into img_spider (img,title,chain,md5) values(%s,%s,%s,%s)", [img, title, chain, md5])
    except:
        pass
    conn.commit()
    cur.close()


def has_md5(conn, md5):
    cur = conn.cursor()
    cur.execute("select img from  img_spider where md5 = %s", [md5])
    res = cur.fetchone()
    cur.close()
    if res is None:
        return False
    return True


def md5_for_file(f, block_size=2 ** 20):
    md5 = hashlib.md5()
    while True:
        data = f.read(block_size)
        if not data:
            break
        md5.update(data)
    return md5.hexdigest()


# ---------------------图片处理区结束---------------------

# ----------------dbmeinv爬虫 开始----------------

def makeMyOpener(head={
    'Connection': 'Keep-Alive',
    'Accept': 'text/html, application/xhtml+xml, */*',
    'Accept-Language': 'en-US,en;q=0.8,zh-Hans-CN;q=0.5,zh-Hans;q=0.3',
    'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko'
}):
    cj = http.cookiejar.CookieJar()
    opener = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(cj))
    header = []
    for key, value in head.items():
        elem = (key, value)
        header.append(elem)
    opener.addheaders = header
    return opener


def crawl_dbmv_loop(index):
    oper = makeMyOpener()
    url = dbmv_url + '/dbgroup/show.htm?pager_offset=%s' % index
    html = oper.open(url)
    bsObj = BeautifulSoup(html)
    conurls = bsObj.select(".img_single a")
    if len(conurls) <= 0:
        print("error:%s" % url)
        return
    for conurl in conurls:
        try:
            html_con = oper.open(conurl.get("href"))
        except:
            print("异常越过：" + conurl)
            continue
        print("html_con:%s" % conurl.get("href"))
        conObj = BeautifulSoup(html_con)
        # print("conObj:%s"%conObj)
        img_urls = conObj.select(".topic-figure img")
        if len(img_urls) <= 0:
            img_urls = conObj.select(".image-wrapper img")
        if len(img_urls) <= 0:
            print("图片没有采集到，请检测页面代码 %s" % conObj)
            break
        title = conObj.select("h1")[0].get_text()
        t1 = time.time()
        chain = str(int(round(t1 * 1000)))
        arry = []
        for imgurl in img_urls:
            link = imgurl.get('src')
            try:
                r = requests.get(link)
                print('正在下载 %s......' % link)
                t = time.time()
                img_name = str(int(round(t * 1000))) + link[-10:]
                filePath = path + img_name
                with open(filePath, 'wb')as jpg:
                    jpg.write(r.content)
                    jpg.close()
                    # print('file:' + filePath)
                    # jpg.flush()
                    f = open(filePath, 'rb')
                    md5 = md5_for_file(f)
                    f.close()
                    d = {'path': filePath, 'name': img_name, 'title': title, 'chain': chain, 'md5': md5}
                    arry.append(d)
            except Exception as e:
                print("img download error,msg:%s" % e)
                continue
        # 检查并保存图片
        handle_img(arry)

    index = int(index) + 1
    print(u'开始抓取下一页')
    print('第 %s 页' % index)
    time.sleep(1)
    crawl_dbmv_loop(index)


# ----------------dbmeinv爬虫 结束----------------

# ----------------jiandan爬虫 开始----------------

def start_jiandan(start, end):
    img_url = []
    dcap = dict(DesiredCapabilities.PHANTOMJS)
    # 从USER_AGENTS列表中随机选一个浏览器头，伪装浏览器
    dcap["phantomjs.page.settings.userAgent"] = (
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.63 Safari/537.36")
    #
    # dcap["phantomjs.page.settings.userAgent"] = (
    #     "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:25.0) Gecko/20100101 Firefox/25.0 "
    # )
    # 不载入图片，爬页面速度会快很多
    # dcap["phantomjs.page.settings.loadImages"] = False

    driver = webdriver.PhantomJS(executable_path=phantomjs,
                                 desired_capabilities=dcap,
                                 service_args=["--ignore-ssl-errors=true", "--ssl-protocol=TLSv1"])
    # 设置10秒页面超时返回，类似于requests.get()的timeout选项，driver.get()没有timeout选项
    # 以前遇到过driver.get(url)一直不返回，但也不报错的问题，这时程序会卡住，设置超时选项能解决这个问题。
    driver.set_page_load_timeout(10)
    # 设置10秒脚本超时时间
    driver.set_script_timeout(10)
    # 隐式等待5秒，可以自己调节
    driver.implicitly_wait(2)
    jiandan_urls = [jiandan_url + "/ooxx/page-{}#comments".format(str(i)) for i in range(start, end)]
    for url in jiandan_urls:
        print("start geting....url:" + url)
        try:
            driver.get(url)
        except Exception as e:
            print("msg:%s" % e)
            continue
        print("end geting....url:" + url)
        data = driver.page_source
        soup = BeautifulSoup(data, "lxml")
        images = soup.select("a.view_img_link")

        for i in images:
            z = i.get('href')
            if str('gif') in str(z):
                pass
            else:
                http_url = "http:" + z
                img_url.append(http_url)
                # print("http:%s" % z)

        for j in img_url:
            try:
                r = requests.get(j)
            except Exception as e:
                print("img download error,msg:%s" % e)
                continue
            print('正在下载 %s......' % j)
            t1 = time.time()
            chain = str(int(round(t1 * 1000)))

            t = time.time()
            img_name = str(int(round(t * 1000))) + j[-10:]
            filePath = path + img_name
            arry = []
            with open(filePath, 'wb')as jpg:
                jpg.write(r.content)
                f = open(filePath, 'rb')
                md5 = md5_for_file(f)
                d = {'path': filePath, 'name': img_name, 'title': '', 'chain': chain, 'md5': md5}
                arry.append(d)
            # 处理图片
            handle_img(arry)


# ----------------jiandan爬虫 结束----------------

# --------------控制台开始---------------
# 抓取dbmeinv
crawl_dbmv_loop(1491)
# start_jiandan(1,200)
