//index.js
const config = require('../../conf.js');
const util = require('../../utils/util.js');

//获取应用实例
var app = getApp()
var mCurrentPage = 0
Page({
  data: {
    title: '',
    content: '',
    hot: '',
    author: ''
  },

  onItemClick: function (event) {
    // var targetUrl = "/pages/image/image";
    // if (event.currentTarget.dataset.url != null)
    //   targetUrl = targetUrl + "?url=" + event.currentTarget.dataset.url;
    // wx.navigateTo({
    //   url: targetUrl
    // });
  },


  onLoad: function (options) {
    // console.log("接收到的参数是str=" + options.id);
    var id = options.id;
    var that = this
    wx.showToast({
      title: '加载中',
      icon: 'loading'
    });
    wx.request({
      url: util.getUrl('/getStory', [{ id: id}, { openId: '' }]),
      success: function (res) {
        if (res == null ||
          res.data == null ||
          res.data.status != 1 ||
          res.data.data == null ||
          res.data.data.length <= 0) {
          return;
        }


        var storyData = res.data.data;

        var tagstr = storyData.tag;
        var tagarry = [];
        if (null != tagstr) {
          tagarry = tagstr.split(',');
        }

        that.setData({
          tags: tagarry,
          content: storyData.content,
          title: storyData.title,
          author: storyData.author,
          hot: storyData.hot,
        });
        wx.hideToast();
      }
    });
  }
})
