importScripts('pako.js');

onmessage = function(e) {
    var obj = e.data;
    for(var key in obj) {
        var zipData = pako.gzip(encodeURIComponent(JSON.stringify(obj[key])), {to: "string"});
        obj[key] = zipData;
    }
    postMessage(obj);
}

