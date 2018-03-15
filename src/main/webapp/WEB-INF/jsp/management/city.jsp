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
        <script type="application/javascript" src="<c:url value='/js/management/city.js'/>"></script>
    </c:param>
    <c:param name="content">
        <h2>Управление городами</h2>
        <hr>

        <div id="editCityModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form id="editCityForm">
                        <input id="cityIdInput" name="id" type="hidden" value="0"/>
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                    aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="editCityLabel">Добавление города</h4>
                        </div>
                        <div id="editCityBody" class="modal-body">
                            <div class="form-group">
                                <label for="cityNameInput" class="control-label">Название:</label>
                                <input id="cityNameInput" name="name" type="text" class="form-control"
                                       placeholder="Название" required>
                            </div>
                            <div class="form-group">
                                <label for="cityCommentInput" class="control-label">Комментарий:</label>
                                <textarea id="cityCommentInput" name="comment" class="form-control"></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button id="editCloseButton" type="button" class="btn btn-default" data-dismiss="modal">
                                Закрыть
                            </button>
                            <button id="editCityButton" type="submit" class="btn btn-primary">Добавить</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div id="removeCityModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form id="removeCityForm"/>">
                        <div class="modal-header alert alert-danger">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                    aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="removeCityLabel">Удаление города</h4>
                        </div>
                        <div id="removeCityBody" class="modal-body">
                            Удалить выбранное город?
                        </div>
                        <div class="modal-footer">
                            <button id="removeCloseButton" type="button" class="btn btn-default" data-dismiss="modal">
                                Закрыть
                            </button>
                            <button id="removeCityButton" type="submit" class="btn btn-primary">Удалить</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div id="citiesTableAlert"></div>

        <div id="filterCityDiv" class="panel panel-default hidden">
            <div class="panel-heading">
                Фильтр пользователей
            </div>
            <div class="panel-body">
                <form id="filterCityForm" class="form-horizontal">
                    <div class="form-group">
                        <label for="filterCityNameInput" class="col-sm-2 control-label">Название:</label>
                        <div class="col-sm-10">
                            <input id="filterCityNameInput" name="name" type="text" class="form-control"
                                   placeholder="Название"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="filterCityRemovedInput" class="col-sm-2 control-label">Удалённые:</label>
                        <div class="col-sm-10">
                            <select id="filterCityRemovedInput" name="removed" class="form-control">
                                <option value="true">Отображать</option>
                                <option value="false" selected="selected">Не отображать</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-2">
                            <button id="filterCitySubmitButton" type="submit" class="btn btn-primary pull-right">
                                Применить
                            </button>
                        </div>
                        <div class="col-sm-10">
                            <button id="filterCityClearButton" type="reset" class="btn btn-default">Очистить</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div id="toolbar" class="btn-group">
            <button id="openAddCityButton" type="button" class="btn btn-default">
                <i class="glyphicon glyphicon-plus"></i>
            </button>
            <button id="openEditCityButton" type="button" class="btn btn-default">
                <i class="glyphicon glyphicon-pencil"></i>
            </button>
            <button id="openRemoveCityButton" type="button" class="btn btn-default">
                <i class="glyphicon glyphicon-trash"></i>
            </button>
        </div>
        <table id="citiesTable"></table>
    </c:param>
</c:import>
