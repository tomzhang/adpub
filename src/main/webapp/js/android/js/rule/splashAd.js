env = {
    wifi: true
}


var api_chelaile = {
    sdkname: function() {
        return 'api_chelaile'
    },

    adurl: function() {
        return {
            url: 'http://dev.chelaile.net.cn/adpub/adv!getCoopenAds.action?${QUERY_STRING}'
        }
    },

    filter: function(data) {

        var array = data.split("YGKJ");
        if (array.length < 2) {
            return null;
        }
        data = array[1];
        if (typeof data == 'string')
            data = eval("a=" + data);

        var rows = data.jsonr.data.ads;

        if (!rows || rows.length == 0)
            return null;
        var row = rows[0];
        var ad = {
			id : row.id,
			isSkip = row.isSkip,
			isDisplay = row.isDisplay,
			duration = row.duration,
			isFullShow = row.isFullShow,
            provider_id: "1",
            link: row.link,
            unfoldMonitorLink: row.unfoldMonitorLink,
            clickMonitorLink: row.clickMonitorLink,
            ad_order: 0,
            openType: row.openType,
            pic: row.pic
        }
        return ad;
    },

    aid : function () {
        return 'chelaile';
    }
}

var api_yd = {
    sdkname: function() {
        return 'api_yd'
    },

    adurl: function() {
        return {
            url: 'http://gorgon.youdao.com/gorgon/request.s?id=e3f49841bbd3ceb0c6a531ca32f4a754&udid=BA8C0E13-F99A-4294-BABA-1489C33E9B6D&imei=BA8C0E13-F99A-4294-BABA-1489C33E9B6D&lla=73.0&llp=p&wifi=&rip=10.168.0.10&imeimd5=305612168A059FC9CCDAC8D95D99E485&ct=2&dct=0&ll=116.403538,39.994026&auidmd5=305612168A059FC9CCDAC8D95D99E485&av=5.50.0&llt=1'

        }
    },

    filter: function(data) {
        if (typeof data == 'string')
            data = eval("a=" + data);

        var rows = data.mainimage ? [data] : data;

        if (!rows || rows.length == 0)
            return null;

        for (var i = 0; i < rows.length; i++) {
            var row = rows[i];

            var ad = {
                provider_id: "11",
                link: row.clk,
                unfoldMonitorLink: row.imptracker.join(";"),
                clickMonitorLink: row.clktrackers.join(";"),
                deeplink: row.deeplink,
                dptrackers: row.dptrackers,
                adType: row.ydAdType,
                // styleName: row.styleName,
                brandIcon: row.iconimage,
                pic: row.mainimage,
                head: row.title,
                ad_order: i,
                subhead: row.text,
                packageName: row.packageName
            }
            console.log("ad = " + ad.link + "  ad  pic ==  " + ad.pic);
            return ad;
        }
        return null;
    },

    aid : function () {
        return 'api_yd';
    }

}

var api_voicead = {

    sdkname: function() {
        return "api_voicead";
    },

    adurl: function() {
        return {
            url: 'http://ws.voiceads.cn/ad/request',
            data: {
                "debug": {
                    "action_type": 2,
                    "landing_ty": 0
                },
                "idfa": "BA8C0E13-F99A-4294-BABA-1489C33E9B6D",
                "idfv": "92669482-B539-4E4C-BCE3-92829225F5BB",
                "ua": "Mozilla/5.0%20(iPhone;%20CPU%20iPhone%20OS%2010_3_3%20like%20Mac%20OS%20X)%20AppleWebKit/603.3.3%20(KHTML,%20like%20Gecko)%20Mobile/14G5037b",
                "net": "0",
                "ip": "10.168.0.10",
                "tramaterialtype": "json",
                "isboot": "1",
                "batch_cnt": "1",
                "density": "2.000000",
                "operator": "46000",
                "lan": "zh-CN",
                "dvh": "480",
                "geo": "116.403538,39.994026",
                "pkgname": "com.chelaile.lite",
                "adunitid": "2D8857EE0D286E80203F7334F8356B1C",
                "adw": "640",
                "orientation": "0",
                "dvw": "320.000000",
                "osv": "10.3.3",
                "ts": "1527056043",
                "appid": "5acf1d60",
                "appname": "车来了",
                "os": "iOS",
                "openudid": "d41d8cd98f00b204e9800998ecf8427e089ec208",
                "devicetype": "1",
                "vendor": "apple",
                "appver": "5.50.0",
                "api_ver": "1.3.8",
                "adh": "960",
                "secure": 0,
                "model": "iPhone5c"
            }
        };
    },

    filter: function(data) {
        if (typeof data == 'string')
            data = eval("a=" + data);

        var rows = data.batch_ma;
        if (!rows || rows.length === 0)
            return null;

        for (var i = 0; i < rows.length; i++) {
            var row = rows[i];

            var ad = {
                provider_id: '12',
                ad_order: i,
                adType: row.adType,
                downloadType: row.download_type,
                packageName: row.package_name,
                head: row.title,
                subhead: row.sub_title,
                pic: row.image,
                brandIcon: row.icon,
                link: row.landing_url,
                deepLink: row.deep_link,
                unfoldMonitorLink: row.impr_url.join(";"),
                clickMonitorLink: row.click_url.join(";")
            }
            return ad;
        }
        return null;
    },

    aid : function () {
        return 'api_voicead';
    }
}




// sdk taks ===================
// 手机调用sdk

var sdk_gdt = {

    adurl: function() {
        return {
            url: "GDTSDK",
            pos: "splash",
            data: {
                appId: "1106616441",
                placementId: "9040714184494018"
            }
        }
    },

    sdkname: function() {
        return "sdk_gdt";
    },

    filter: function(list) {
        return list;
    }
}


var sdk_baidu = {

    adurl: function() {
        return {
            url: "BaiduSDK",
            pos: "splash",
            data: {
                appId: "",
                placementId: "5826172"
            }
        }
    },

    sdkname: function() {
        return "sdk_baidu";
    },

    filter: function(msg) {
        return msg;
    }
}

var sdk_toutiao = {

    adurl: function() {
        return {
            url: "TOUTIAOSDK",
            pos: "splash",
            data: {
                appId: "",
                placementId: "800673832"
            }
        }
    },

    sdkname: function() {
        return "sdk_toutiao";
    },

    asEntity: function(ad) {
        // TODO
        return {};
    },

    filter: function(ad) {
        return ad;
    }
}

var sdk_voicead = {

    adurl: function() {
        return {
            url: "IFLYSDK",
            pos: "splash",
            data: {
                appId: "",
                placementId: "D028C0ADDDBC38952DA01241B4939E64"
            }
        }
    },

    sdkname: function() {
        return "sdk_voicead";
    },

    asEntity: function(ad) {
        // TODO
        return {}
    },

    filter: function(ad) {
        return ad;
    }
}

function ads() {
    return {
        traceInfo: {
            ip: '192.168.100.100'
        },
        urls: {
            exposeUrl: 'http://atrace.chelaile.net.cn/exhibit',
            clickUrl: 'http://atrace.chelaile.net.cn/click',
            closeUrl: 'http://atrace.chelaile.net.cn/close'
        },
        timeouts: [3000, 2000],
        tasks: [
            [api_yd]
        ]
    }
}

module.exports = ads;

console.log('splash loaded');
