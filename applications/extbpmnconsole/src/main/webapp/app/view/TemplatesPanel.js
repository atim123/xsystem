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

Ext.define('BPMN.view.TemplatesPanel', {
    extend: 'Ext.tree.Panel',
    requires: ['BPMN.model.Definitions'],
    alias: 'widget.BPMN-templatesPanel',
    title: 'Шаблоны',
    rootVisible: false,
    initComponent: function () {
        this.tbar = [
            {
                text: 'Обновить',
                action: 'update'
            },
            {
                text: 'Опубликовать',
                action: 'uploaddefinition'
            }
        ];

        this.store = Ext.create('Ext.data.TreeStore', {
            model: 'BPMN.model.Definitions',
            proxy: {
                type: 'ajax',
                url: 'rest/db/repo/definitions/list',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            },
            root: {
                expanded: true,
                text: '.'
            },
            lazyFill: true
        });

        this.columns = [{
                xtype: 'treecolumn', //this is so we know which column will show the tree
                text: 'ID',
                width: 200,
                sortable: true,
                dataIndex: 'id',
                locked: true
            }, {
                text: 'Название',
                width: 150,
                dataIndex: 'name'
            },
            {
                text: 'Удалить',
                //width: 40,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/delete.png',
                handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
                   BPMN.app.fireEvent('undeploydefinitions',record);
                },
                // Only leaf level tasks may be edited
                isDisabled: function (view, rowIdx, colIdx, item, record) {
                    return record.data.type!=='definitions';
                }
            },
            {
                text: 'Запустить',
                //width: 40,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/mark_active.png',
                handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
                   BPMN.app.fireEvent('startproces',record);
                },
                // Only leaf level tasks may be edited
                isDisabled: function (view, rowIdx, colIdx, item, record) {
                    return record.data.type!=='process';
                }
            },
            {
                text: 'Выгрузить',
                //width: 40,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/Load.png',
                handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
                    BPMN.app.fireEvent('loadfromdb',record);
                },
                // Only leaf level tasks may be edited
                isDisabled: function (view, rowIdx, colIdx, item, record) {
                    return record.data.type!=='definitions';
                }
            }
        ];

        this.callParent(arguments);
    }
});


