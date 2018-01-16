import pymysql
import uuid

def write_db():
    conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='', db='yujian')
    cur = conn.cursor()
    cur.execute("SELECT * FROM img limit 10")
    for r in cur.fetchall():
        print(r)
    cur.close()
    conn.close()

# print(uuid.uuid1())
#
# s = "bcccc.jpg"
# pos = s.rfind(".")
# print()
# s[:pos] # "C:/Python27/1"
import hashlib
def md5_for_file(f, block_size=2**20):

    md5 = hashlib.md5()
    while True:
        data = f.read(block_size)
        if not data:
            break
        md5.update(data)
    return md5.hexdigest()
f = open("/Users/wangqing/Documents/code/wechat/img/15160543197480aft9n.jpg", 'rb')
print(md5_for_file(f))
