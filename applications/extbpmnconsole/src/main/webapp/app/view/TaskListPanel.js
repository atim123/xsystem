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

Ext.define('BPMN.view.TaskListPanel', {
    extend: 'Ext.grid.Panel',
    requires: ['BPMN.model.TaskList'],
    alias: 'widget.BPMN-taskListPanel',
    title: 'Задачи',
    features: [{ftype:'grouping',
                hideGroupedHeader: true
            }],
    
    initComponent: function () {
        
        this.tbar = [
            {
                text: 'Обновить',
                action: 'update'
            }];
        
        this.store = Ext.create('Ext.data.Store', {
            model: 'BPMN.model.TaskList',
            proxy: {
                type: 'ajax',
                url: 'rest/db/repo/task/list',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            },
            groupField: 'definitionname',
            autoLoad: true
        });
        
        this.columns= [
            {  text: 'Шаблон',
               dataIndex: 'definitionname'
            },
            {
            text: 'id',
            flex: 1,
            dataIndex: 'id'
           },
           {
            text: 'Имя Задачи',
            flex: 1,
            dataIndex: 'taskname'
           },
           {
            text: 'activityid',
            flex: 1,
            dataIndex: 'activityid'
           },
           {
            text: 'formkey',
            flex: 1,
            dataIndex: 'formkey'
           },
           {
                text: 'Исполнить',
                //width: 40,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/mark_active.png',
                handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
                   BPMN.app.fireEvent('complitetask',record);
                }
            },
            {
               text: 'Контекст',
                //width: 40,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/table_16x16.gif',
                handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
                      var processinstance= record.get("processinstance");    
                      BPMN.app.fireEvent('processcontext',processinstance);
                } 
            }
       ];
       
       
       this.callParent(arguments);
   }
});



