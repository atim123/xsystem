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

Ext.define('BPMN.view.ProcessInstansesPanel', {
    extend: 'Ext.grid.Panel',
    requires: ['BPMN.model.ProcessInstanses'],
    alias: 'widget.BPMN-processInstansesPanel',
    title: 'Процессы',
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
            model: 'BPMN.model.ProcessInstanses',
            proxy: {
                type: 'ajax',
                url: 'rest/db/repo/processinstanses/list',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            },
            groupField: 'defname',
            autoLoad: true
        });
        
        this.columns= [
            {  text: 'Шаблон',
               dataIndex: 'defname'
            },
            {
            text: 'id',
            flex: 1,
            dataIndex: 'id'
           },
           {
            text: 'Имя процесса',
            flex: 1,
            dataIndex: 'procname'
           },
           {
                text: 'Остановить',
                //width: 40,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/delete.png',
                handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
                    BPMN.app.fireEvent('stopproces',record);
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
                      var processinstance= record.get("id");   
                      BPMN.app.fireEvent('processcontext',processinstance);
                } 
            }
       ];
       this.callParent(arguments);
   }
});


//Ext.grid.Panel
//ProcessInstansesPanel


