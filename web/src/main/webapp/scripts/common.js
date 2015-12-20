/**
 * Create namespace (packages context) under APP.
 *
 * @require Prototype framework
 * @author chalermpong.ch@ocean.co.th
 */
var APP = { };

/**
 * @require Prototype framework
 */
APP.namespace = function(context, properties) {
    if (Object.isString(context)) {
        var curr_ctx = this;

        $A(context.split('.')).each(function(s, index) {
            if (!curr_ctx[s]) curr_ctx[s] = {};
            curr_ctx = curr_ctx[s];
        });

        try {
            if (Object.isFunction(properties)) {
                curr_ctx.__callback = properties;
                curr_ctx.__callback();
            }
            /*else for (var property in properties) curr_ctx[property] = properties[property];*/
            else Object.extend(curr_ctx, properties);
        }
        catch (e) { }

        return curr_ctx;
    }
};

document.on('dom:loaded', function() {
    try { document.on('contextmenu', function(event) { event.stop(); }); }
    catch (ignored) { }

    //skin: 'sam',
    YUI({  gallery: 'gallery-2012.05.16-20-37' }).use('node-menunav', 'gallery-uuid', 'gallery-layout', 'gallery-busyoverlay', function(Y) {
        var body = Y.one('#main-layout');
        body.plug(Y.Plugin.BusyOverlay);
        document.on('state:busy', function(event) { body.busy.show(); });
        document.on('state:normal', function(event) { body.busy.hide(); });
        //document.fire('layout:init', { layout: layout })

        //var node = Y.one('body');
        //node.plug(Y.Plugin.BusyOverlay);
        //
        //Y.on('click', function()
        //    {
        //        node.busy.toggleVisible();
        //    },
        //    '#toggle');
    });

});

APP.namespace('commands', {
    reloadPage: function() { location.reload(); },
    backHome: function() { location.href = APP.actions.home; },
    go: function(address) { location.href = address; },
    logout: function(url) {
        if (confirm('ต้องการออกจากระบบ?')) {
            document.fire('state:busy');
            new Ajax.Request(url || APP.actions.logout, {
                method: 'POST',
                asynchronous: false,
                onSucess: function(transport) { }
            });
            location.href = APP.actions.home;
        }
    }
});
APP.namespace('commons.func', {
    lpadZero : function(o){
        var zero = "00000000";
        return zero.substring(0,(zero.length-o.value.toString().length))+o.value.toString()
    },
    removeZero : function(o){
        var s = o.value.toString();
        return s.replace(/^0+/, "");
    },
    trim : function(o){
        return o.value.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
    },
    dateYearNumberlock : function(o){
    	YUI().use('node',function(Y){
    		Y.on('keydown', function(e){
    			//number = 96-105
    			// / = 111
    			//backspace = 8
    			//del = 46
    			//arrow left = 37
    			//arrow right = 39
    			//number keyboard 48-57
    			var len = this.get('value').length;
    			var val = this.get('value');
    			var code = e.keyCode;
    			if(code==37||code==39||code==8||code==46){
    				return;
    			}else{
    				if(len==0 && ((code==96)||(code==97)||(code==48)||(code==49)) ){
    					return;
    				}
    				if(len==1){
    					if(val=='0'){
    						if((code>=97&&code<=105)||(code>=49&&code<=57)){
    							return;
    						}else{
    							e.halt();
    						}
    					}else if(val=='1'){
    						if((code==96)||(code==97)||(code==98)||(code>=48&&code<=50)){
    							return;
    						}else{
    							e.halt();
    						}
    					}
    				}
    				if(len==2 && ((code==111||code==191)) ){
    					return;
    				}
    				if(len==3 && ((code==98||code==50)) ){
    					return;
    				}
    				if( ((len==4)||(len==5)||(len==6)||(len==7)) && ((code>=96&&code<=105)||(code>=48&&code<=57)) ){
    					return;
    				}else{
    					e.halt();
    				}
    			}
			},o);
    		
    		Y.on('contextmenu', function(e) {
				e.halt();
			}, o);
    	});
    },
    receiptNumberLock : function(o){
        YUI().use('node',function(Y){
	        var s = o.value.toString();
	        Y.on('keydown', function(e){
	        	var charcode = (Prototype.Browser.IE || Prototype.Browser.Gecko || Prototype.Browser.Opera)? e.keyCode : e.charCode;
	            if( 
	            	( (Event.KEY_RETURN == charcode) ||
	            		(charcode > 47 && charcode < 58) || 
	            		(charcode > 95 && charcode < 106 ) || 
	            		(charcode > 36 && charcode < 41) || 
	            		charcode == 8 || 
	            		charcode ==46)
	                && 
	                (e.altKey !== true && e.shifKey !== true && e.metaKey !== true ) )
	            	{
	                    return;
	                } else {
	                    e.halt();
	                }	
	        }, s);
	
	        //restrict right click on defined content area
	        Y.on('contextmenu', function(e) {
	            e.halt();
	        }, s);
	    });
    },
    spaceRemove : function(o){	
    	var nameCol = [];
		var tmpS='';
		if (o.value.length > 0){
			for(var i=0; i<=o.value.length;i++){
				if(o.value.substr(i,1) != ' '){					
					tmpS = tmpS + o.value.substr(i,1);
					if(o.value.substr(i+1,1) == ' '){				
						nameCol.push(tmpS);
						tmpS='';
					}
				}
			}	
			if(tmpS.length > 0) nameCol.push(tmpS);			
		}
		var returnString ='';
		for(var j=0;j<=nameCol.length-1;j++){
			if(j != nameCol.length-1){
				returnString = returnString + nameCol[j] + ' ';
			}else{
				returnString = returnString + nameCol[j];
			}			
		}		
		return returnString;
	}/*,
    dateFormat: function(o){
    	YUI({ combine: false, throwFail: true, lang: 'th-TH' })
    		.use('datatype-date', 'datatype-date-parse', function(Y){
    			var t = Y.DataType.Date,
                value = (o.value && o.value.sub(/T/, ' ')) || '',
                date = t.parse(value);
                a = t.format(date, { format: '%d/%m/%Y' })
            return (a);
    	});
    }*/
});
APP.namespace('response.handlers', {
    success: function(transport, callback, owner, resetState, exceptionHandler) {
        var text = transport.responseText,
            result = transport.responseJSON,
            resetState = (undefined === resetState) ? true : (true == resetState),
            console = console;
        try {
            if (console && console.debug) console.debug(result || text);
            transport.error = false;
            if (result) {
                if (result.errorMessage) {
                    if (Object.isFunction(exceptionHandler)) {
                        if (owner) exceptionHandler.bind(owner)(result.errorMessage);
                        else exceptionHandler(result.errorMessage);
                    }
                    else alert(result.errorMessage);
                    transport.error = true;
                    return;
                }
                if (result.message) alert(result.message);
                if (result.redirectURL) {
                    APP.commands.go(result.redirectURL);
                    return;
                }
                if (Object.isFunction(callback)) {
                    if (owner) callback.bind(owner)(result ? result : text);
                    else callback(result ? result : text);
                }
            } 
            else if (text.include('meta name="keywords" content="LOGIN_PAGE')) {
                alert('ระบบไม่ได้ถูกใช้งานนานเกินกำหนด จำเป็นต้องเข้าระบบใหม่')
                APP.commands.reloadPage();
            }
            else if (text.include('meta name="keywords" content="ACCESS_FORBIDDEN')) {
                transport.error = true;
                alert('สิทธิการใช้งานไม่เพียงพอ');
            }
            else if (text.include('meta name="keywords" content="ACCESS_DENIED')) {
                alert('ไม่สามารถเข้าใช้งานระบบได้ในช่วงเวลานี้');
                APP.commands.logout();
            }
            else if (text.include('<title>Struts Problem Report</title>')) {
                transport.error = true;
                alert('Unexpected error');
            }
            else if (text.include('<title>Error</title>')) {
                transport.error = true;
                alert('Error');
            }
            else if (text.include('<title>Struts Problem Report</title>')) {
                var msg = '';
                transport.error = true;
                text.scan(/<strong>(.+:.+)<\/strong>/, function(match) { msg += (match[0].stripTags() + '\r\n'); });
                if (msg) alert(msg.strip());
                else alert('Unhandled exception!');
            }
            else if (0 == transport.status) {
                alert('Service is temporarily unavailable');
                APP.commands.reloadPage();
            }
            else if (text && 200 == transport.status) {
                callback(text);
            }
            else {
                transport.error = true;
                alert('ERR-#{status}: Unrecognized server response.\r\n\r\n#{responseText}'.interpolate(transport));
            }
        }
        catch (e) { if (console && console.error) console.error('common.js: ' + e); }
        finally { if (resetState) document.fire('state:normal'); }
    }
});

(function() {
    YUI({ combine: false, throwFail: true, lang: 'th-TH' })
        .use('datatype-date', 'datatype-number',
             'datatype-date-parse', function(Y) {
        APP.namespace('formatters', {
            dateTime: function(o) {
                if (!o.value) return '';
                var value = o.value || '';
                if (o.value) {
                    if (Prototype.Browser.IE) value = value.sub(/-/, '/');
                    else if (Prototype.Browser.WebKit) value = value.sub(/T/, ' ');
                }
                var t = Y.DataType.Date,
                    date = t.parse(value),
                    a = t.format(date, { format: '%a %d %b ' }),
                    b = date.getFullYear() + 543,
                    c = t.format(date, { format: ' %H:%M:%S' });
                return (a + b + c);
            },
            number: function(o) {
                return Y.DataType.Number.format(o.value || 0, { thousandsSeparator: ',' });
            },
            number2digit: function(o) {
                return Y.DataType.Number.format(o.value, { thousandsSeparator: ',', decimalPlaces: 2});
            },
            dateEN: function(o) {
            	if (!o.value) return '';
                var value = o.value || '';
                if (o.value) {
                    if (Prototype.Browser.IE) value = value.sub(/-/, '/');
                    else if (Prototype.Browser.WebKit) value = value.sub(/T/, ' ');
                }
                var t = Y.DataType.Date,
                    date = t.parse(value),
                    a = t.format(date, { format: '%d/%m/%Y' });
                return (a);
            },
            dateTimeEN: function(o) {
            	if (!o.value) return '';
                var value = o.value || '';
                if (o.value) {
                    if (Prototype.Browser.IE) value = value.sub(/-/, '/');
                    else if (Prototype.Browser.WebKit) value = value.sub(/T/, ' ');
                }
                var t = Y.DataType.Date,
                    date = t.parse(value),
                    a = t.format(date, { format: '%d/%m/%Y' }),
                    c = t.format(date, { format: ' %H:%M:%S' });
                return (a+c);
            },
            dateTH: function(value, formatDM) {
            	var fmt = Object.isUndefined(formatDM)?'%d %b ':formatDM;
                if (!value) return '';
                if (value||'') {
                    if (Prototype.Browser.IE) value = value.sub(/-/, '/');
                    else if (Prototype.Browser.WebKit) value = value.sub(/T/, ' ');
                }
                var t = Y.DataType.Date,
                    date = t.parse(value),
                    a = t.format(date, { format: fmt }),
                    b = date.getFullYear() + 543;
                return (a + b);
            },
            dateTHExceptYear: function(value, formatDM) {
            	var fmt = Object.isUndefined(formatDM)?'%d %b ':formatDM;
                if (!value) return '';
                if (value||'') {
                    if (Prototype.Browser.IE) value = value.sub(/-/, '/');
                    else if (Prototype.Browser.WebKit) value = value.sub(/T/, ' ');
                }
                var t = Y.DataType.Date,
                    date = t.parse(value),
                    a = t.format(date, { format: fmt });
                    //b = date.getFullYear() + 543;
                //return (a + b);
                return a;
            },
            datePart: function(value, fmt) {
            	var format = Object.isUndefined(fmt)?'%d/%m/%Y %H:%M:%S':fmt;
                if (!value) return '';
                if (value||'') {
                    if (Prototype.Browser.IE) value = value.sub(/-/, '/');
                    else if (Prototype.Browser.WebKit) value = value.sub(/T/, ' ');
                }
                var t = Y.DataType.Date,
                    date = t.parse(value),
                    a = t.format(date, { format: format });
                return (a);
            },
            currency: function(value, decimalPlaces, suffix) {
                return Y.DataType.Number.format(value, { thousandsSeparator: ',', decimalPlaces: decimalPlaces, suffix: suffix });
            }
        });
    });
})();
