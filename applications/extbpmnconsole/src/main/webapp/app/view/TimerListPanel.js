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



Ext.define('BPMN.view.TimerListPanel', {
    extend: 'Ext.grid.Panel',
    requires: ['BPMN.model.TimerList'],
    alias: 'widget.BPMN-timerListPanel',
    title: 'Таймер',
    features: [{ftype:'grouping',
                hideGroupedHeader: true
            }],
    
    initComponent: function () {
        
        this.tbar = [
            {
                text: 'Обновить',
                action: 'update'
            },
            '-',
            {
                text: 'Таймер старт',
                action: 'timerStart'
            },
            {
                text: 'Таймер стоп',
                action: 'timerStop'
            }
        ];
        
        this.store = Ext.create('Ext.data.Store', {
            model: 'BPMN.model.TaskList',
            proxy: {
                type: 'ajax',
                url: 'rest/db/repo/timer/list',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            },
            groupField: 'processdefinition',
            autoLoad: true
        });
        
        this.columns= [
            {  text: 'Шаблон процесса',
               dataIndex: 'processdefinition'
            },
            {
            text: 'id',
            flex: 1,
            dataIndex: 'id'
           },
            {
            text: 'Старт',
            flex: 1,
            dataIndex: 'created'
           },
            {
            text: 'Завершение',
            flex: 1,
            dataIndex: 'responsetime'
           },
           {
               text: 'Источник',
            flex: 1,
            dataIndex: 'srcactivty'
           },
           {
            text: 'Приемник',
            flex: 1,
            dataIndex: 'targetactivity'
           },
           {
                text: 'Исполнить',
                //width: 40,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/mark_active.png',
                handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
                            BPMN.app.fireEvent('pushtimer',record);
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
                      var processinstance= record.get("process");    
                      BPMN.app.fireEvent('processcontext',processinstance);
                } 
            }
       ];
       
       
       this.callParent(arguments);
   }
});


