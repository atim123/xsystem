/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// step 1
App.Router = Backbone.Router.extend({
	routes: {
		'' : 'index'
	},
 
	index: function() {
		console.log('Index!!!');	
	}
 
});


