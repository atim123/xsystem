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
    extend: 'Ext.Panel',
    requires: ['BPMN.view.BpmnEditorDiagram',
        'BPMN.view.BpmnEditorPalette',
        'BPMN.view.BpmnPropertyGrid'
    ],
    alias: 'widget.BPMN-mainpanel',
    title: 'Редактор Бизнес процесов',
    layout: {
        type: 'border',
        padding: 5
    },
    initComponent: function () {
        this.tbar = [
            {text: 'Обновить',
                action: 'update'
            },
            {text: 'test',
                action: 'testaction'
            },
            {text: 'Файлы',
                menu: {
                    xtype: 'menu',
                    plain: true,
                    items: [
                        {text: "Создать",
                         action:"newdiagram"
                        },
                        {text: "Открыть",
                        action: "loaddefenition"
                        },
                        "-",
                        {text: "Сохранить",
                            action: "savedefenition"
                        }
                    ]
                }
            }
        ];

        this.items = [
            {xtype: 'BPMN-BpmnEditorPalette',
                region: 'west',
                split: true,
                collapsible: true
            },
            {xtype: 'BPMN-BpmnEditorDiagram',
                region: 'center',
                title: 'center'
                        //width: 110
            },
            {xtype: 'BPMN-BpmnPropertyGrid',
                region: 'east',
                title: 'Свойства',
                width: 200,
                split: true,
                collapsible: true,
                layout: "fit"
            }
        ];


        this.callParent(arguments);
    }
});

