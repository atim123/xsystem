/* 
 * Copyright (C) 2017 Andrey Timofeev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

Ext.define('BPMN.view.SubscriptionEditor', {
   extend: 'Ext.window.Window',
   requires: ['BPMN.view.SubscriptionGrid'],
    frame: true,
    layout: 'fit',
    alias: 'widget.BPMN-SubscriptionEditor',
    width: 500,
    esizable: true,
    height: 400, 
    modal:true,
initComponent: function () {
       var self=this;
       self.grid=Ext.create('BPMN.view.SubscriptionGrid', {inspectedObject:this.getModel()});
       
       self.items =[this.grid];
       
       self.buttons = [
            {
                text: 'Закрыть',
                scope: self,
                handler: self.close
            }
        ];
        this.callParent(arguments);   
    }
});
