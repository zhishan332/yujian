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
import codecs
import random

from textrank4zh import TextRank4Keyword, TextRank4Sentence

# import pysocks
path = '/Users/wangqing/Documents/code/wechat/img/'

'根据接口从服务器读取整个页面'
phantomjs = r'/Users/wangqing/Documents/program/phantomjs-2.1.1-macosx/bin/phantomjs'
dcap = dict(DesiredCapabilities.PHANTOMJS)
# 从USER_AGENTS列表中随机选一个浏览器头，伪装浏览器
dcap["phantomjs.page.settings.userAgent"] = (
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.63 Safari/537.36")

driver = webdriver.PhantomJS(executable_path=phantomjs,
                             desired_capabilities=dcap,
                             service_args=["--ignore-ssl-errors=true", "--ssl-protocol=TLSv1"])
# 设置10秒页面超时返回，类似于requests.get()的timeout选项，driver.get()没有timeout选项
# 以前遇到过driver.get(url)一直不返回，但也不报错的问题，这时程序会卡住，设置超时选项能解决这个问题。
driver.set_page_load_timeout(20)
# 设置10秒脚本超时时间
driver.set_script_timeout(20)
# 隐式等待5秒，可以自己调节
driver.implicitly_wait(2)


def write_db(title, author, content, tag, summary, hot):
    conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='', db='yujian', charset="utf8")
    cur = conn.cursor()
    cur.execute("insert into story (title,content,summary,author,tag,hot) values(%s,%s,%s,%s,%s,%s)",
                [title, content, summary, author, tag, hot])
    conn.commit()
    cur.close()


def request(url):
    try:
        driver.get(url)
    except Exception as e:
        print("msg:%s" % e)
        return None
    data = driver.page_source
    return data


def parse(html):
    soup = BeautifulSoup(html, "lxml")
    title = soup.h1.string
    author = soup.select_one(".PostIndex-authorName").get_text()
    tagHtml = soup.select(".PostIndex-topics a")
    tags = ''
    tag_num = 0
    for dd in tagHtml:
        tags += dd.get_text()
        tags += ','
        tag_num += 1
        if tag_num >= 4 or len(tags) > 15:
            break

    tags = tags[:-1]
    conhtml = soup.select('.RichText')[0]
    con = ''
    summary = ''
    i = 0
    for st in conhtml.children:
        if len(summary) < 100:
            try:
                summary += st.get_text().strip()
            except Exception as e:
                print("msg:%s" % e)
            # print(summary)
        con += str(st)
        i += 1
    hot = random.randint(70, 300)
    write_db(title, author, con, tags, summary, hot)
    print("保存成功%s" % title)
    #
    # tr4w = TextRank4Keyword()
    # tr4w.analyze(text=conhtml.string(), lower=True, window=2)  # py2中text必须是utf8编码的str或者unicode对象，py3中必须是utf8编码的bytes或者str对象
    #
    # print('关键短语：')
    # for phrase in tr4w.get_keyphrases(keywords_num=20, min_occur_num=2):
    #     print(phrase)
    #
    # tr4s = TextRank4Sentence()
    # tr4s.analyze(text=str(conhtml.string(), lower=True, source='all_filters')
    # print( '摘要：' )
    # for item in tr4s.get_key_sentences(num=3):
    #     print(item.index, item.weight, item.sentence)


html = request("https://zhuanlan.zhihu.com/p/26646318")
parse(html)
