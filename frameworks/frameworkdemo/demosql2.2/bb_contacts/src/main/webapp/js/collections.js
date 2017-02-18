/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//---------------------------------------------------

App.Collections.Contacts = Backbone.Collection.extend({
	model: App.Models.Contact,
	url: 'rest/contact/'
});
