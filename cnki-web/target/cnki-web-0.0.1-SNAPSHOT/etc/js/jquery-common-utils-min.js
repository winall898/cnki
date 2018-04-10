(function(a){a.extend(a,{jsonToString:function(f){switch(typeof(f)){case"object":var d=[];if(f instanceof Array){for(var e=0,b=f.length;e<b;e++){d.push(a.jsonToString(f[e]))}return"["+d.join(",")+"]"}else{if(f instanceof RegExp){return f.toString()}else{for(var c in f){d.push('"'+c+'":'+a.jsonToString(f[c]))}return"{"+d.join(",")+"}"}}case"function":return"function() {}";case"number":return f.toString();case"string":return'"'+f.replace(/(\\|\")/g,"\\$1").replace(/\n|\r|\t/g,function(g){return("\n"==g)?"\\n":("\r"==g)?"\\r":("\t"==g)?"\\t":""})+'"';case"boolean":return f.toString();default:return f.toString()}},getThis:function(b){if(a.browser.msie){return b.srcElement}else{return b.target}},offHeight:function(){if(a.browser.msie){return document.documentElement.clientHeight}return window.innerHeight},copyText:function(j,i){if(window.clipboardData){window.clipboardData.setData("Text",j)}else{if(window.netscape){try{netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect")}catch(g){alert("\u60a8\u7684\u6d4f\u89c8\u5668\u5b89\u5168\u9650\u5236\u9650\u5236\u60a8\u8fdb\u884c\u590d\u5236\u64cd\u4f5c\uff0c\u8bf7\u624b\u52a8\u590d\u5236\u4ee3\u7801\u3002");return}var d=Components.classes["@mozilla.org/widget/clipboard;1"].createInstance(Components.interfaces.nsIClipboard);if(!d){return}var k=Components.classes["@mozilla.org/widget/transferable;1"].createInstance(Components.interfaces.nsITransferable);if(!k){return}k.addDataFlavor("text/unicode");var h=new Object();var f=new Object();var h=Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);var c=j;h.data=c;k.setTransferData("text/unicode",h,c.length*2);var b=Components.interfaces.nsIClipboard;if(!d){return false}d.setData(k,null,b.kGlobalClipboard)}}alert("\u590d\u5236\u6210\u529f\uff01")},stopDefault:function(b){if(b&&b.preventDefault){b.preventDefault()}else{window.event.returnValue=false}return false},stopBubble:function(b){if(b&&b.stopPropagation){b.stopPropagation()}else{window.event.cancelBubble=true}},setCookie:function(e){var d=a.extend({key:"",value:"",path:"/",domain:"",expireDay:-1,today:false,secure:false},e);if(d.key!=""){var c=new Array();c.push(d.key);c.push("=");c.push(encodeURIComponent(d.value));c.push("; path=");c.push(d.path);if(d.domain!=""){c.push("; domain=");c.push(d.domain)}if(d.expireDay>0){var b=new Date();if(d.today){b.setHours(0,0,0,0)}b.setTime(b.getTime()+d.expireDay*24*60*60*1000+1000);c.push("; expires=");c.push(b.toGMTString())}else{if(d.expireDay==0){var b=new Date();b.setTime(b.getTime()-1);c.push("; expires=");c.push(b.toGMTString())}}if(d.secure){c.push("; secure")}document.cookie=c.join("")}},getCookie:function(b){var c=document.cookie.match("(?:^|;)\\s*"+b.replace(/([.*+?^${}()|[\]\/\\])/g,"\\$1")+"=([^;]*)");return c?decodeURIComponent(c[1]):false},delCookie:function(b){a.setCookie({key:b,expireDay:0})}})})(jQuery);
