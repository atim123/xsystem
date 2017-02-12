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

Ext.define('BPMN.view.MbeansOpDlg', {
  extend: 'Ext.window.Window',
  requires: ['BPMN.view.MbeansOpPanel'],
    frame: true,
    layout: 'fit',
    alias: 'widget.BPMM-defListDlg',
    width: 500,
    esizable: true,
    height: 400,
    modal: true,
    
    initComponent: function () {
      var self=this;
      var grid=  Ext.create('BPMN.view.MbeansOpPanel', {});
      
      self.items =[grid];
      
      self.buttons = [
            {
                text: 'ОК',
                scope: this,
                handler:function(){
                    
                    var rows=grid.getSelection(); 
                    if (rows&&rows.length>0){
                       var row=rows[0];
                       self.cbSelecton(row);
                       self.close();
                    } else {
                       alert("Операция не выбрана"); 
                    }
                    
                }
             },
            {
                text: 'Отменить',
                scope: self,
                handler: self.close
            }
        ];
        this.callParent(arguments);
    }
});



