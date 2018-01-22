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
    // console.log(event)
    let id = event.currentTarget.dataset.index;
    console.log(id)
    wx.navigateTo({
      url: '../storydetail/storydetail?id=' + id,
      success: function (res) {
        // success
      },
      fail: function () {
        // fail
      },
      complete: function () {
        // complete
      }
    })
  },

  // loadMore: function( event ) {
  //   var that = this
  //   requestData( that, mCurrentPage + 1 );
  // },

  onReachBottom: function () {
    // console.log('onLoad')
    this.onLoad();
  },

  onLoad: function () {
    console.log('onLoad')
    this.loadStoryList();
  },
  loadStoryList() {
    if (this.data.end) {
      return;
    }
    var that = this
    wx.showToast({
      title: '加载中',
      icon: 'loading'
    });
    wx.request({
      url: util.getUrl('/findStory', [{ start: mCurrentPage }, { openId: '' }]),
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
        var storyData = res.data;
        var storyList = [];
        for (var i = 0; i < storyData.data.length; i++) {
          let obj = storyData.data[i];

        var tagstr = obj.tag;
        var tagarry=[];
        if(null != tagstr){
          tagarry = tagstr.split(',');
        }

        storyList.push({
            id: obj.id,
            title: obj.title,
            author: obj.author,
            hot: obj.hot,
            tags: tagarry,
            // nodes:obj.content
            nodes: [{
              name: 'div',
              attrs: {
                class: 'div_class',
                style: 'line-height: 24px; font-size:12px;'
              },
              children: [{
                type: 'text',
                text: obj.summary
              }]
            }]
          });
          // itemList.push({ title: obj.title, summary: obj.summary,hot:obj.hot,author:author });
        }

        that.setData({
          items: that.data.items.concat(storyList),
          hidden: true,
          // loadmorehidden:false,
        });
        wx.hideToast();
      }
    });
  }
})
