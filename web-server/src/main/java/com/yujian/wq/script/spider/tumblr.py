# -*- encoding:gbk -*-
__author__ = 'henan'
import json
import time
import re
import os
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
import pycurl
from io import *
import wget

# import pysocks
path = '/Users/wangqing/Documents/code/wechat/img/'

proxies = {
    "http": "http://127.0.0.1:1087",
    "https": "https://127.0.0.1:1087",
}

'���ݽӿڴӷ�������ȡ����ҳ��'
phantomjs = r'/Users/wangqing/Documents/program/phantomjs-2.1.1-macosx/bin/phantomjs'
dcap = dict(DesiredCapabilities.PHANTOMJS)
# ��USER_AGENTS�б������ѡһ�������ͷ��αװ�����
dcap["phantomjs.page.settings.userAgent"] = (
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.63 Safari/537.36")

driver = webdriver.PhantomJS(executable_path=phantomjs,
                             desired_capabilities=dcap,
                             service_args=["--ignore-ssl-errors=true", "--ssl-protocol=TLSv1"])
# ����10��ҳ�泬ʱ���أ�������requests.get()��timeoutѡ�driver.get()û��timeoutѡ��
# ��ǰ������driver.get(url)һֱ�����أ���Ҳ����������⣬��ʱ����Ῠס�����ó�ʱѡ���ܽ��������⡣
driver.set_page_load_timeout(20)
# ����10��ű���ʱʱ��
driver.set_script_timeout(20)
# ��ʽ�ȴ�5�룬�����Լ�����
driver.implicitly_wait(2)


# ---------------------ͼƬ��������ʼ---------------------
def handle_img(arry):
    conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='', db='yujian', charset="utf8")
    isOk = True
    for data in arry:
        md5str = data['md5']
        if has_md5(conn, md5str):
            isOk = False
            break
    print('�Ƿ�����д�룺%s' % isOk)
    if isOk:
        for data in arry:
            write_db(conn, data['name'], data['title'], data['chain'], data['md5'])
    else:
        for data in arry:
            os.remove(data['path'])
    conn.close()


def write_db(conn, img, title, chain, md5):
    cur = conn.cursor()
    cur.execute("insert into img_spider (img,title,chain,md5) values(%s,%s,%s,%s)", [img, title, chain, md5])
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


def getHtml(username):
    overview_url = 'http://' + username + '.tumblr.com/api/read/json?start=0&num=200'
    try:
        driver.get(overview_url)
    except Exception as e:
        print("msg:%s" % e)
        return None
    data = driver.page_source

    return data
    # f=open('/Users/wangqing/Documents/code/wechat/tutuserver/yujian/web-server/src/main/java/com/yujian/wq/script/test/tumblr_normal.txt','r')
    # return f.read()


def getResourceUrl(htmlStr):
    '��ȡ���е���Ƶ��ַ'
    data = json.loads(htmlStr[106:-22])

    for i in range(0, len(data["posts"])):
        # if data["posts"][i]["type"]=="video":
        #     pass
        #     videoSourceCode = data["posts"][i]["video-player-500"]
        #     videoList.append(videoSourceCode)
        if data["posts"][i]["type"] == "photo":
            # image=data["posts"][i]["photo-url-1280"]

            # imageList.append(image)
            if data["posts"][i]["photos"] != None:
                imageList = []
                for img in range(0, len(data["posts"][i]["photos"])):
                    imageList.append(data["posts"][i]["photos"][img]["photo-url-1280"])
                title = data["posts"][i]["slug"]
                print("��������:" + title)
                arry = []
                t1 = time.time()
                chain = str(int(round(t1 * 1000)))
                for j in imageList:
                    t = time.time()
                    img_name = str(int(round(t * 1000))) + j[-10:]
                    filePath = path + img_name

                    try:
                        r = requests.get(j, proxies=proxies)
                        # r = requests.get(j,proxies=dict(http='socks5://23.236.76.64:2224',
                        #          https='socks5://23.236.76.64:2224'))
                        # Download(j, filePath)
                        # page = require('webpage').create();
                        # page.viewportSize = {
                        #     width: 1920,
                        #     height: 1080
                        # }
                        # wget.download(j, out=filePath)
                    except Exception as e:
                        print("img download error,msg:%s" % e)
                        continue
                    print('�������� %s......' % j)
                    with open(filePath, 'wb')as jpg:
                        jpg.write(r.content)
                        jpg.flush()
                        jpg.close()
                        f = open(filePath, 'rb')
                        md5 = md5_for_file(f)
                        d = {'path': filePath, 'name': img_name, 'title': title, 'chain': chain, 'md5': md5}

                        contains = False
                        for lastobj in arry:
                            mdd = lastobj['md5']
                            if mdd == md5:
                                contains = True
                        if contains == False:
                            arry.append(d)

                # ��鲢����ͼƬ
                try:
                    handle_img(arry)
                except Exception as e1:
                    print(" handle_img error,msg:%s" % e1)
                continue


if __name__ == '__main__':
    # f = getHtml('sexsusu')
    f = getHtml("theoreocat")
    getResourceUrl(f)
