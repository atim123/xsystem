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

Ext.define('BPMN.view.InterfacesEditor', {
   extend: 'Ext.window.Window',
   requires: ['BPMN.view.InterfacesPanel'],
    frame: true,
    layout: 'fit',
    alias: 'widget.BPMN-interfacesEditor',
    width: 500,
    esizable: true,
    height: 400, 
    modal:true,
    title: 'Операции',
initComponent: function () {
       var self=this;
       self.grid=Ext.create('BPMN.view.InterfacesPanel', {inspectedObject:this.getModel()});
       
       self.items =[this.grid];
       
       self.buttons = [
            {
                text: 'Закрыть',
                scope: self,
                handler: self.close
            },
             {
                text: 'Выбрать',
                scope: self,
                handler: function(){
                   var ref= self.grid.getSelectedOperation();
                   if (ref){
                       if (self.saveCb){
                        self.saveCb(ref);
                       }
                       self.close();
                   } else {
                       alert("Операция не выбрана"); 
                    }
                }
            }
        ];
        this.callParent(arguments);   
    }
});



