//index.js
const config = require('../../conf.js');
const util = require('../../utils/util.js');

//获取应用实例
var app = getApp()
var mCurrentPage = 0
Page({
  data: {
    items: [],
    end: false,
    loading: false,
    // loadmorehidden:true,
    plain: false
  },

  onItemClick: function (event) {
    // var targetUrl = "/pages/image/image";
    // if (event.currentTarget.dataset.url != null)
    //   targetUrl = targetUrl + "?url=" + event.currentTarget.dataset.url;
    // wx.navigateTo({
    //   url: targetUrl
    // });
  },

  // loadMore: function( event ) {
  //   var that = this
  //   requestData( that, mCurrentPage + 1 );
  // },

  onReachBottom: function () {
    // console.log('onLoad')
    // this.setData({
    //   hidden: false,
    // });
    // mCurrentPage++
    this.loadImgList();
  },

  onLoad: function () {
    console.log('onLoad')
    // mCurrentPage++
    this.loadImgList();
  },
  loadImgList() {
    if (this.data.end) {
      return;
    }
    var that = this
    wx.showToast({
      title: '加载中',
      icon: 'loading'
    });
    wx.request({
      url: util.getUrl('/loadChainData', [{ start: mCurrentPage}, { openId: '' }]),
      success: function (res) {
        mCurrentPage++;
        if (res == null ||
          res.data == null ||
          res.data.status != 1 ||
          res.data.data == null) {
          return;
        }

        if (res.data.data.length <= 0) {
          that.setData({
            end: true
          })
          return;
        }

        //将获得的各种数据写入itemList，用于setData
        var itemList = [];
        var imgObj = res.data;
        for (var i = 0; i < imgObj.data.length; i++) {
          let obj = imgObj.data[i];
          let child = obj.chainList;
          let num = obj.num;
          var childList = [];
          for (var nn = 0; nn < child.length; nn++) {
            let childData = child[nn];
            childList.push({ img: util.imgUrlFix(childData.img) })
          }
          if(childList.length == num){
            itemList.push({ img: util.imgUrlFix(obj.img), title: obj.title, total: obj.num, childList: childList });
          }
        }


        that.setData({
          items: that.data.items.concat(itemList),
          hidden: true,
          // loadmorehidden:false,
        });
        wx.hideToast();
      }
    });
  }
})
