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
    print("����ɹ�%s" % title)
    #
    # tr4w = TextRank4Keyword()
    # tr4w.analyze(text=conhtml.string(), lower=True, window=2)  # py2��text������utf8�����str����unicode����py3�б�����utf8�����bytes����str����
    #
    # print('�ؼ����')
    # for phrase in tr4w.get_keyphrases(keywords_num=20, min_occur_num=2):
    #     print(phrase)
    #
    # tr4s = TextRank4Sentence()
    # tr4s.analyze(text=str(conhtml.string(), lower=True, source='all_filters')
    # print( 'ժҪ��' )
    # for item in tr4s.get_key_sentences(num=3):
    #     print(item.index, item.weight, item.sentence)


html = request("https://zhuanlan.zhihu.com/p/26646318")
parse(html)
