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

Ext.define('BPMN.view.StartDlg', {
    extend: 'Ext.Window',
    alias: 'widget.BPMN-startDlg',
    requires: ['BPMN.view.TextEditor'],
    title: 'Выполнить',
    width: 400,
    height: 175,
    layout: 'fit',
    autoShow: true,
    modal: true,
    initComponent: function () {
        
        var dataField = Ext.create("BPMN.view.TextEditor", {});
        this.items = //[dataField];
        
        [{xtype: "panel",
             layout: 'fit',
             scrollable: true,
                items: [dataField]
            }];
        
        
        this.buttons = [
            {
                text: 'Выполнить',
                action: this.doAction
            },
            {
                text: 'Отменить',
                scope: this,
                handler: this.close
            }
        ];
        this.callParent(arguments);
        
        this.setData=function(data){
            dataField.setData(data);
        };
        
        this.getData=function(){
            return dataField.getData();
        }
    }
    
});
