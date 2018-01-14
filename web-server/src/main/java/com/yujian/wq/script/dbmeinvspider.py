# https://www.dbmeinv.com/dbgroup/current.htm?cid=0

from urllib.request import urlopen
import urllib.request

import requests
from bs4 import BeautifulSoup
import os, sys, time
import http.cookiejar

path = 'I:/doc/tutu/img/2/'


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


def crawl_loop(index):
    oper = makeMyOpener()
    url = 'http://www.dbmeinv.com/dbgroup/show.htm?pager_offset=%s' % index
    html = oper.open(url)
    bsObj = BeautifulSoup(html)
    conurls = bsObj.select(".img_single a")
    if len(conurls) <= 0:
        print("error:%s" % url)
        return
    for conurl in conurls:
        html_con = oper.open(conurl.get("href"))
        print("html_con:%s" % conurl.get("href"))
        conObj = BeautifulSoup(html_con)
        # print("conObj:%s"%conObj)
        img_urls = conObj.select(".topic-figure img")
        if len(img_urls) <= 0:
            img_urls = conObj.select(".image-wrapper img")
        if len(img_urls) <= 0:
            print("图片没有采集到，请检测页面代码 %s" % conObj)
            break

        for imgurl in img_urls:
            link = imgurl.get('src')
            try:
                r = requests.get(link)
                print('正在下载 %s......' % link)
                filePath = path + link[-20:]
                with open(filePath, 'wb')as jpg:
                    jpg.write(r.content)
            except Exception as e:
                print("img download error,msg:%s" % e)
                continue
    index = int(index) + 1
    print(u'开始抓取下一页')
    print('第 %s 页' % index)
    time.sleep(1)
    crawl_loop(index)
crawl_loop(458)
