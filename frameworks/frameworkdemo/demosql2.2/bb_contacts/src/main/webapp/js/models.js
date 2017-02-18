/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


App.Models.Contact = Backbone.Model.extend({
    
    url: 'rest/contact/',
    
    validate: function (attrs) {
        if (!attrs.first_name || !attrs.last_name) {
            return "Имя и фамилия обязательны для заполнения!";
        }

        if (!attrs.email_address) {
            return "Пожалуйста введите валидный Email.";
        }
    }
});
