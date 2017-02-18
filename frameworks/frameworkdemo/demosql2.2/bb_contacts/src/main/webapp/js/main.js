/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



Backbone.sync = function (method, model, options) {

    if (model.loading && model.loading.method === method) {
        model.loading.xhr.abort();
    }
    var url;
    var data = {};
    var isCollection = (model instanceof Backbone.Collection)
    var isModel = (model instanceof Backbone.Model);
    if (isCollection || isModel) {
        if (_.isFunction(model.url)) {
            url = model.url(method)
        } else {
            url = model.url;
        }
    }

    if (isCollection && method === "read") {
        url = url + "list";
    } else if (isModel && method === "read") {
        var id = model.get("id");
        url = url + "get/" + id;
    } else if (isModel && method === "update") {
        var id = model.get("id");
        if (id) {
            url = url + "update/" + id;
        } else {
            url = url + "create";
        }
        data = {data: model.toJSON()};
    } else if (isModel && method === "create") {
        url = url + "create";

        data = {data: model.toJSON()};
    } else if (isModel && method === "delete") {
        var id = model.get("id");
        url = url + "delete/" + id;
    }
    //var data=  JSON.stringify(model.toJSON());
    //console.log(data);
    var xhr =
            $.ajax({
                type: 'POST',
                url: url,
                dataType: 'json',
                data: JSON.stringify(data),
                success: function (ret) {
                    // Проверка наличия ошибки
                    console.log('Проверка наличия ошибки 152');
                    if (ret.success) {
                        console.log('Запрос завершен');
                        options.success(ret.data);
                    } else {
                        console.log('Ошибка запроса');
                        if (options.error) {
                            options.error(ret);
                        }
                    }
                },
                error: function (req_obj, msg, error) {
                    console.log('Ошибка запроса 123');
                    //  if (options.error) {
                    //      options.error(res);
                    //  }

                }
            });



    console.log(url);
    console.log(model);

    model.loading = {
        method: method,
        xhr: xhr
    };
    return xhr;
    // console.log(opt);
};


//-----------------------------------------------------------------------------

(function () {
    window.App = {
        Models: {},
        Collections: {},
        Views: {},
        Router: {}
    };

    window.vent = _.extend({}, Backbone.Events);

    App.template = function (id) {
        return Handlebars.compile($('#' + id).html());
    };

}());
