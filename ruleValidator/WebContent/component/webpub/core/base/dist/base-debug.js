define("ecwebpub/base/1.0.0/aspect-debug", [], function(require, exports) {
    // Aspect
    // ---------------------
    // Thanks to:
    //  - http://yuilibrary.com/yui/docs/api/classes/Do.html
    //  - http://code.google.com/p/jquery-aop/
    //  - http://lazutkin.com/blog/2008/may/18/aop-aspect-javascript-dojo/
    // 在指定方法执行前，先执行 callback
    exports.before = function(methodName, callback, context) {
        return weave.call(this, "before", methodName, callback, context);
    };
    // 在指定方法执行后，再执行 callback
    exports.after = function(methodName, callback, context) {
        return weave.call(this, "after", methodName, callback, context);
    };
    // Helpers
    // -------
    var eventSplitter = /\s+/;
    function weave(when, methodName, callback, context) {
        var names = methodName.split(eventSplitter);
        var name, method;
        while (name = names.shift()) {
            method = getMethod(this, name);
            if (!method.__isAspected) {
                wrap.call(this, name);
            }
            this.on(when + ":" + name, callback, context);
        }
        return this;
    }
    function getMethod(host, methodName) {
        var method = host[methodName];
        if (!method) {
            throw new Error("Invalid method name: " + methodName);
        }
        return method;
    }
    function wrap(methodName) {
        var old = this[methodName];
        this[methodName] = function() {
            var args = Array.prototype.slice.call(arguments);
            var beforeArgs = [ "before:" + methodName ].concat(args);
            this.trigger.apply(this, beforeArgs);
            var ret = old.apply(this, arguments);
            this.trigger("after:" + methodName, ret);
            return ret;
        };
        this[methodName].__isAspected = true;
    }
});

define("ecwebpub/base/1.0.0/base-debug", [ "./aspect-debug", "ecwebpub/class/1.0.0/class-debug", "ecwebpub/events/1.0.0/events-debug" ], function(require, exports, module) {
    /**
	 * Base 相当于object
	 */
    var Class = require("ecwebpub/class/1.0.0/class-debug");
    var Events = require("ecwebpub/events/1.0.0/events-debug");
    var Aspect = require("./aspect-debug");
    //			var Attribute = require('./attribute');
    module.exports = Class.create({
        Implements: [ Events, Aspect ],
        prop: {},
        /**
		 * reload为boolean值，指定是否需要重新绘制
		 */
        set: function(param1, param2, reload) {
            if (this.prop[param1] != undefined) {
                this.prop[param1] = param2;
            } else {
                this.prop["attrs"][param1] = param2;
            }
            if (reload) {
                this.change();
            }
        },
        get: function(param) {
            if (this.prop[param]) {
                return this.prop[param];
            } else {
                if (this.prop["attrs"]) {
                    return this.prop["attrs"][param];
                } else {
                    return undefined;
                }
            }
        },
        change: function() {}
    });
});