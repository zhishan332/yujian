from PIL import Image
import os

# path = 'I:/doc/tutu/img/2/'
# path = '/Users/wangqing/Documents/code/wechat/img/'
path = '/Users/wangqing/Documents/code/my/data/deeplearn/img/'

for file in os.listdir(path):

    try:
        checkfile = os.path.join(path, file)
        fp = open(checkfile, 'rb')
        img = Image.open(fp)
        x, y = img.size
        fp.close()
        if x < 500 or y < 600:
            os.remove(checkfile)
            print("remove file %s" % checkfile)
    except:
        pass
