<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="l" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<c:import url="/WEB-INF/jsp/main.jsp">
    <c:param name="link">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-table.min.css'/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-multiselect.css'/>"/>
    </c:param>
    <c:param name="script">
        <script type="application/javascript" src="<c:url value='/js/bootstrap-table.js'/>"></script>
        <script type="application/javascript" src="<c:url value='/js/bootstrap-table-cookie.min.js'/>"></script>
        <script type="application/javascript" src="<c:url value='/js/locale/bootstrap-table-ru-RU.js'/>"></script>
        <script type="application/javascript" src="<c:url value='/js/bootstrap-validator.js'/>"></script>
        <script type="application/javascript" src="<c:url value='/js/bootstrap-multiselect.js'/>"></script>
        <script type="application/javascript" src="<c:url value='/js/js.cookie.js'/>"></script>
        <script type="application/javascript" src="<c:url value='/js/management/unit.js'/>"></script>
    </c:param>
    <c:param name="content">
        <h2>Управление подразделениями</h2>
        <hr>

        <div id="editUnitModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form id="editUnitForm" method="post" action="<c:url value='/updateUnitJson.action'/>">
                        <input id="unitIdInput" name="unit.id" type="hidden" value="0"/>
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                    aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="editUnitLabel">Добавление подразделения</h4>
                        </div>
                        <div id="editUnitBody" class="modal-body">
                            <div class="form-group">
                                <label for="unitNameInput" class="control-label">Название:</label>
                                <input id="unitNameInput" name="unit.name" type="text" class="form-control"
                                       placeholder="Название" required>
                            </div>
                            <div class="form-group">
                                <label for="unitTypeInput" class="control-label">Тип:</label>
                                <select id="unitTypeInput" name="unit.type.id" class="form-control" data-selected="bar"
                                        data-selected-error="Необходимо выбрать тип" required>
                                    <option value="0">--- Не выбран ---</option>
                                    <c:forEach var="type" items="${ types }">
                                        <option value="${ type.id }"><c:out value="${ type.name }"/></option>
                                    </c:forEach>
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                            <div class="form-group">
                                <label for="unitCityInput" class="control-label">Город:</label>
                                <select id="unitCityInput" name="unit.city.id" class="form-control" data-selected="bar"
                                        data-selected-error="Необходимо выбрать город" required>
                                    <option value="0">--- Не выбрано ---</option>
                                    <c:forEach var="city" items="${ cities }">
                                        <option value="${ city.id }"><c:out value="${ city.name }"/></option>
                                    </c:forEach>
                                </select>
                                <div class="help-block with-errors"></div>
                            </div>
                            <div class="form-group">
                                <label for="unitCommentInput" class="control-label">Комментарий:</label>
                                <textarea id="unitCommentInput" name="unit.comment" class="form-control"></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button id="editCloseButton" type="button" class="btn btn-default" data-dismiss="modal">
                                Закрыть
                            </button>
                            <button id="editUnitButton" type="submit" class="btn btn-primary">Добавить</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div id="removeUnitModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form id="removeUnitForm" method="post" action="<c:url value='/removeUnitJson.action'/>">
                        <input id="removeUnitIdInput" name="unitId" type="hidden" value="0"/>
                        <div class="modal-header alert alert-danger">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                    aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="removeUnitLabel">Удаление подразделения</h4>
                        </div>
                        <div id="removeUnitBody" class="modal-body">
                            Удалить выбранное подразделение?
                        </div>
                        <div class="modal-footer">
                            <button id="removeCloseButton" type="button" class="btn btn-default" data-dismiss="modal">
                                Закрыть
                            </button>
                            <button id="removeUnitButton" type="submit" class="btn btn-primary">Удалить</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div id="unitsTableAlert"></div>

        <div id="filterUnitDiv" class="panel panel-default hidden">
            <div class="panel-heading">
                Фильтр подразделений
            </div>
            <div class="panel-body">
                <form id="filterUnitForm" class="form-horizontal">
                    <div class="form-group">
                        <label for="filterUnitNameInput" class="col-sm-2 control-label">Название:</label>
                        <div class="col-sm-10">
                            <input id="filterUnitNameInput" name="name" type="text" class="form-control"
                                   placeholder="Название"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="filterUnitTypesInput" class="col-sm-2 control-label">Типы:</label>
                        <div class="col-sm-10">
                            <select id="filterUnitTypesInput" name="types" class="form-control" multiple>
                                <c:forEach var="type" items="${ types }">
                                    <option value="${ type.id }">${ type.name }</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="filterUnitCitiesInput" class="col-sm-2 control-label">Города:</label>
                        <div class="col-sm-10">
                            <select id="filterUnitCitiesInput" name="cities" class="form-control" multiple>
                                <c:forEach var="city" items="${ cities }">
                                    <option value="${ city.id }">${ city.name }</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="filterUnitRemovedInput" class="col-sm-2 control-label">Удалённые:</label>
                        <div class="col-sm-10">
                            <select id="filterUnitRemovedInput" name="removed" class="form-control">
                                <option value="true">Отображать</option>
                                <option value="false" selected="selected">Не отображать</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-2">
                            <button id="filterUnitSubmitButton" type="submit" class="btn btn-primary pull-right">
                                Применить
                            </button>
                        </div>
                        <div class="col-sm-10">
                            <button id="filterUnitClearButton" type="reset" class="btn btn-default">Очистить</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div id="toolbar" class="btn-group">
            <button id="openAddUnitButton" type="button" class="btn btn-default">
                <i class="glyphicon glyphicon-plus"></i>
            </button>
            <button id="openEditUnitButton" type="button" class="btn btn-default">
                <i class="glyphicon glyphicon-pencil"></i>
            </button>
            <button id="openRemoveUnitButton" type="button" class="btn btn-default">
                <i class="glyphicon glyphicon-trash"></i>
            </button>
        </div>
        <table id="unitsTable"></table>
    </c:param>
</c:import>
