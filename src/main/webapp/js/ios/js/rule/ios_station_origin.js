


env = {
    wifi: true
}

var api_chelaile = {
    sdkname: function() {
        return 'api_chelaile'
    },

    adurl_ios: function() {
        return {
            type:"banner",
            url: 'https://api.chelaile.net.cn/adpub/adv!getStationAds.action?${QUERY_STRING}',
            data:{
                dataFormater:this.dataFormater
            }
        }
    },
    
    dataFormater : {
        parse:function(data) {
            var array = data.split("YGKJ");
            if (array.length < 2) {
                return null;
            }
            data = array[1];
            if (typeof data == 'string')
                data = eval("a=" + data);
            var rows = data.jsonr.data.ads;
            console.log("rows="+rows);
            return rows;
        }
    },
    
    
    filter_ios: function(list) {
        if(Array.isArray(list) && list.length > 0) {
            var info = list[list.length - 1].info;
            info.adid = info.id;
            list[list.length - 1].info = info;
            return [list[list.length - 1]];
        }
        return [];
    },
    
    aid : function () {
        return 'api_chelaile';
    }
}

// sdk taks ===================
// ææºè°ç¨sdk

var sdk_gdt = {

    sdkname: function() {
        return "sdk_gdt";
    },
    
    adurl_ios: function() {
        return {
            url: "GDTSDK",
            pos: "banner",
            data: {
                "appId":"1105595946",
                "placementId":"9060637344635371"
            }
        }
    },

    filter_ios : function(list) {
        if(Array.isArray(list) && list.length > 0) {
            var info = list[0].info;
            info.bannerInfo = {'bannerType':4,'color':'#66ccff','sloganColor':'#000000','button':{'buttonPic':'https://image3.chelaile.net.cn//3c8d25de6d6b481989e596bad3d42cff'}};
            info.pic = '';
            list[0].info = info;
            return [list[0]];
        }
        return [];
    },

    aid : function () {
        return 'sdk_gdt';
    },
    
    adStyle : function() {
        return 2;
    }
}

var sdk_baidu = {

    sdkname: function() {
        return "sdk_baidu";
    },
    
    adurl_ios: function() {
        return {
            url: "BaiduSDK",
            pos: "banner",
            data: {
                "appId":"bbf9b3b5",
                "placementId":"5826171"
            }
        }
    },
    
    filter_ios : function(list) {
        if(Array.isArray(list) && list.length > 0) {
            var info = list[0].info;
            info.bannerInfo = {'bannerType':4,'color':'#66ccff','sloganColor':'#000000','button':{'buttonPic':'https://image3.chelaile.net.cn//3c8d25de6d6b481989e596bad3d42cff'}};
            info.pic = '';
            list[0].info = info;
            return [list[0]];
        }
        return [];
    },

    aid : function () {
        return 'sdk_baidu';
    },
    
    adStyle : function() {
        return 2;
    }
}

var sdk_toutiao = {

    adurl_ios: function() {
        return {
            url: "TOUTIAOSDK",
            pos: "banner",
            data: {
                "appId":"5001451",
                "placementId":"901451521"
            }
        }
    },

    sdkname: function() {
        return "sdk_toutiao";
    },

    filter_ios : function(list) {
        if(Array.isArray(list) && list.length > 0) {
            var info = list[0].info;
            info.bannerInfo = {'bannerType':4,'color':'#66ccff','sloganColor':'#000000','button':{'buttonPic':'https://image3.chelaile.net.cn//3c8d25de6d6b481989e596bad3d42cff'}};
            info.pic = '';
            list[0].info = info;
            return [list[0]];
        }
        return [];
    },
    
    aid : function () {
        return 'sdk_toutiao';
    },
    
    adStyle : function() {
        return 1;
    }
}

var sdk_ifly = {

    sdkname : function() {
        return "sdk_ifly";
    },
    
    adurl_ios : function() {
        return {
            url:"IFLYSDK",
            pos:"banner",
            data:{
                "appId":"5acf1d60",
                "placementId":"70EB266B2C8429B0B6C10D9B6F9BFA93"
            }
        }
    },

    filter_ios : function(list) {
        if(Array.isArray(list) && list.length > 0) {
            var info = list[0].info;
            info.bannerInfo = {'bannerType':4,'color':'#66ccff','sloganColor':'#000000','button':{'buttonPic':'https://image3.chelaile.net.cn//3c8d25de6d6b481989e596bad3d42cff'}};
            info.pic = '';
            list[0].info = info;
            return [list[0]];
        }
        return [];
    },
    
    aid : function () {
        return 'sdk_ifly';
    },
    
    adStyle : function() {
        return 2;
    }
}



function ads() {

//var ads = [api_chelaile, sdk_inmobi, sdk_toutiao, sdk_gdt, sdk_voicead, sdk_baidu];
    return {
      traceInfo : {
          traceid: '${TRACEID}',
          pid: '15'
      },
        timeouts: ${TIMEOUTS},
        tasks: ${TASKS}
    }
}

console.log('splash loaded');



var getAds = require('./fetch');
function loadAds(userdata, callback) {
    if(getAds) {
        getAds(ads(), userdata, callback);
    }
}
module.exports = loadAds;
