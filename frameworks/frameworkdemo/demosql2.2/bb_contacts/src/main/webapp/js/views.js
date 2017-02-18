/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


App.Views.App = Backbone.View.extend({
    initialize: function () {
        vent.on('contact:edit', this.editContact, this);
        var addContactView = new App.Views.AddContact({collection: App.contacts});
        var allContactsView = new App.Views.Contacts({collection: App.contacts}).render();
        $('#allContacts').append(allContactsView.el);
    },
    editContact: function (contact) {
        //var editContactView = new App.Views.EditContact({model: contact});
        //$(document.body).append(editContactView.el);

        var editContactView = new App.Views.EditContact({model: contact});
        $('.editContact').html(editContactView.el);


    }

});
/*
 |---------------------------------------------------
 | Add Contact View
 |---------------------------------------------------
 */

App.Views.AddContact = Backbone.View.extend({
    el: '#addContact',
    initialize: function () {
        this.first_name = $('#first_name');
        this.last_name = $('#last_name');
        this.email_address = $('#email_address');
        this.description = $('#description');
    },
    events: {
        'submit': 'addContact'
    },
    addContact: function (e) {
        e.preventDefault();
        var newContact = this.collection.create({
            first_name: this.first_name.val(),
            last_name: this.last_name.val(),
            email_address: this.email_address.val(),
            description: this.description.val()
        }, {wait: true});
        this.clearForm();
    },
    clearForm: function () {
        this.first_name.val('');
        this.last_name.val('');
        this.email_address.val('');
        this.description.val('');
    }
});
/*
 |---------------------------------------------------
 |  All Contacts View
 |---------------------------------------------------
 */
App.Views.Contacts = Backbone.View.extend({
    tagName: 'tbody',
    initialize: function () {
        this.collection.on('add', this.addOne, this);
    },
    render: function () {
        this.collection.each(this.addOne, this);
        return this;
    },
    addOne: function (contact) {
        var contactView = new App.Views.Contact({model: contact});
        console.log(contactView.render().el);
        this.$el.append(contactView.render().el);
    }
});
/*
 |---------------------------------------------------
 | Single Contact View
 |---------------------------------------------------
 */


App.Views.Contact = Backbone.View.extend({
    initialize: function () {
        this.model.on('destroy', this.unrender, this);
        this.model.on('change', this.render, this);

    },
    tagName: 'tr',
    events: {
        'click a.delete': 'deleteContact',
        'click a.edit': 'editContact'
    },
    deleteContact: function () {
        this.model.destroy();
    },
    editContact: function () {

        vent.trigger('contact:edit', this.model);
        /*
         var editContactView = new App.Views.EditContact({ model: this.model });
         $(document.body).append(editContactView.el);
         */
    },
    unrender: function () {
        this.remove(); // this.$el.remove()
    },
    template: App.template('contactTemplate'),
    render: function () {
        this.$el.html(this.template(this.model.toJSON()));
        return this;
    }

});
/*
 |---------------------------------------------------
 | Edit Contact View
 |---------------------------------------------------
 */
App.Views.EditContact = Backbone.View.extend({
    template: App.template('editContactTpl'),
    initialize: function () {
        this.render();

        this.form = this.$('form');
        this.first_name = this.form.find('#edit_first_name');
        this.last_name = this.form.find('#edit_last_name');
        this.email_address = this.form.find('#edit_email_address');
        this.description = this.form.find('#edit_description');

    },
    /*        events: {
     'submit form': 'submit'
     },*/

    events: {
        'submit #edit-contact-form': 'submit',
        'click button.cancel': 'cancel'
    },
    cancel: function () {
        this.remove();    
    },
    
    submit: function (e) {
        e.preventDefault();

        this.model.save({
            first_name: this.first_name.val(),
            last_name: this.last_name.val(),
            email_address: this.email_address.val(),
            description: this.description.val()
        });
        this.remove();
    },
    render: function () {
        var html = this.template(this.model.toJSON());
        this.$el.html(html);
        return this;
    }

});




