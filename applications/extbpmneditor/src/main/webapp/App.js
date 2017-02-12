/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.application({
    requires: ['Ext.container.Viewport',
               'BPMN.view.MainPanel'
              ],
    name: 'BPMN',

    appFolder: 'app',
    controllers: [
       'BPMNEditor'
    ], 
    launch: function() {
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            items: [
                {xtype: 'BPMN-mainpanel'}
            ]
        });
    }
});

