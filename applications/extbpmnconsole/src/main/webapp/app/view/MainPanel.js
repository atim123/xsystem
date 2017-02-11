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
Ext.define('BPMN.view.MainPanel', {
    extend: 'Ext.tab.Panel',
    requires: ['BPMN.view.TemplatesPanel',
               'BPMN.view.ProcessInstansesPanel',
               'BPMN.view.TaskListPanel',
               'BPMN.view.TimerListPanel'
              ],
    alias: 'widget.BPMN-mainpanel',
    title: 'Консоль управления Бизнес процесами',
    header: {
        titlePosition: 2,
        titleAlign: 'center'
    },
    tabPosition: 'left',
    tabBar: {
        border: false
    },
    defaults: {
        textAlign: 'left',
        bodyPadding: 15
    },
    initComponent: function () {
        this.items = [{xtype: 'BPMN-templatesPanel'},
                      {xtype: 'BPMN-processInstansesPanel'},
                      {xtype: 'BPMN-taskListPanel'},
                      {xtype: 'BPMN-timerListPanel'}
                    ];
        

        this.callParent(arguments);
    }
});

