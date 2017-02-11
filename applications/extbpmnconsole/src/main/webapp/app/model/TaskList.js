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
Ext.define('BPMN.model.TaskList', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id',type: 'string'},
        { name: 'taskname',type: 'string'},
        { name: 'formkey',type: 'string'},
        { name: 'owner',type: 'string'},
        { name: 'processinstance',type: 'string'},
        { name: 'activityid',type: 'string'},
        { name: 'processdefinition',type: 'string'},
        { name: 'processname',type: 'string'},
        { name: 'definitionid',type: 'string'},
        { name: 'definitionname',type: 'string'}
    ]
    
});



