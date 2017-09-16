<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="l" uri="http://shiro.apache.org/tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<c:import url="/WEB-INF/jsp/main.jsp">
<c:param name="link">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-table.min.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-multiselect.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-datetimepicker.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/journal/bso.css'/>"/>
</c:param>
<c:param name="script">
	<script type="application/javascript" src="<c:url value='/js/moment-with-locales.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/bootstrap-table.min.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/bootstrap-table-cookie.min.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/locale/bootstrap-table-ru-RU.min.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/bootstrap-validator.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/bootstrap-multiselect.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/bootstrap-datetimepicker.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/js.cookie.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/journal/bso.js'/>"></script>
</c:param>
<c:param name="content">
	<h2>Журнал БСО</h2>
	<hr>

	<div id="actModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<form id="actForm" method="post" action="<c:url value='/updateActJson.action'/>">
					<input id="actTypeIdxInput" type="hidden" name="act.type.idx" value=""/>
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 id="actModalTitle" class="modal-title">Акт</h4>
					</div>
					<div id="actModalBody" class="modal-body">
						<div class="row form-group">
							<div class="col-xs-6">
								<label for="actTypeInput" class="control-label">Тип акта:</label>
								<select id="actTypeInput" name="act.type.id" class="form-control" data-selected="foo" data-selected-error="Необходимо выбрать тип акта" required disabled>
									<option value="0">--- Не выбран ---</option>
									<c:forEach var="type" items="${ types }">
										<option value="${ type.id }">${ type.fullName }</option>
									</c:forEach>
								</select>
								<div class="help-block with-errors"></div>
							</div>
							<div class="col-xs-6">
							</div>
						</div>
						<div class="row form-group">
							<div class="col-xs-6">
								<label for="actSenderInput" class="control-label">Откуда:</label>
								<select id="actSenderInput" name="act.sender.id" class="form-control" data-selected-error="Необходимо выбрать подразделение отправителя" required disabled>
									<option value="0">--- Не выбрано ---</option>
								</select>
								<div class="help-block with-errors"></div>
							</div>
							<div class="col-xs-6">
								<label for="actRecipientInput" class="control-label">Куда:</label>
								<select id="actRecipientInput" name="act.recipient.id" class="form-control" data-selected-error="Необходимо выбрать подразделение получателя" required disabled>
									<option value="0">--- Не выбрано ---</option>
								</select>
								<div class="help-block with-errors"></div>
							</div>
						</div>
						<div class="form-group">
							<label for="actCommentInput" class="control-label">Комментарий:</label>
							<textarea id="actCommentInput" name="act.comment" class="form-control"></textarea>
						</div>
						<div class="form-group">
							<label for="actPacksTable" class="control-label">БСО:</label>
							<table id="actPacksTable" data-table-error="Необходимо ввести хотя бы одну пачку БСО"></table>
							<div class="help-block with-errors"></div>
						</div>
						<div id="packAddAlert" class="form-group">
						</div>
						<div class="form-group">
							<div class="row">
								<div class="col-xs-3">
									<input id="packSeriesInput" class="form-control" placeholder="Серия" disabled/>
								</div>
								<div class="col-xs-3">
									<input id="packNumberFromInput" class="form-control" placeholder="Номер с" disabled/>
								</div>
								<div class="col-xs-3">
									<input id="packNumberToInput" class="form-control" placeholder="Номер по" disabled/>
								</div>
								<div class="col-xs-3">
									<input id="packAmountInput" class="form-control" placeholder="Количество" disabled/>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-9">
								</div>
								<div class="col-xs-3">
									<button id="packAddButton" type="button" class="btn btn-default btn-block" disabled>Добавить</button>
								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button id="actCloseButton" type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
						<button id="actSubmitButton" type="submit" class="btn btn-primary">Ввести</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div id="actionModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="actionForm" method="post" action="<c:url value='/doActionJson.action'/>">
					<input id="bsoIdInput" type="hidden" name="bsoId" value=""/>
					<input id="actionIdInput" type="hidden" name="actionId" value=""/>
					<input id="actionIdxInput" type="hidden" name="actionIdx" value=""/>
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 id="actionModalTitle" class="modal-title">Действие</h4>
					</div>
					<div id="actionModalBody" class="modal-body">
						<div id="issueModalBody" class="hidden">
							<div class="form-group">
								<label for="actionInsuredInput" class="control-label">Страхователь:</label>
								<input id="actionInsuredInput" name="insured" class="form-control" data-required-error="Необходимо ввести страхователя" disabled/>
								<div class="help-block with-errors"></div>
							</div>
							<div class="form-group">
								<label for="actionPremiumInput" class="control-label">Страховая премия:</label>
								<input id="actionPremiumInput" name="premium" class="form-control" data-required-error="Необходимо ввести страховую премию" data-format-error="Неверный формат суммы" disabled/>
								<div class="help-block with-errors"></div>
							</div>
						</div>
						<div id="corruptModalBody" class="hidden">
							Отметить БСО испорченным?
						</div>
						<div id="registerModalBody" class="hidden">
							Отметить БСО зарегистрированным в системе?
						</div>
					</div>
					<div class="modal-footer">
						<button id="actionCloseButton" type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
						<button id="actionSubmitButton" type="submit" class="btn btn-primary" disabled>Выполнить</button>
					</div>
				</form>
			</div>
		</div>
	</div>

    <div id="bsosTableAlert"></div>

    <div id="filterBsoDiv" class="panel panel-default hidden">
    	<div class="panel-heading">
    		Фильтр БСО
    	</div>
    	<div class="panel-body">
			<form id="filterBsoForm" class="form-horizontal">
				<div class="form-group">
					<label for="filterBsoSeriesInput" class="col-sm-2 control-label">Серия:</label>
					<div class="col-sm-10">
						<input id="filterBsoSeriesInput" name="filter.series" type="text" class="form-control" placeholder="Серия"/>
					</div>
				</div>
				<div class="form-group">
					<label for="filterBsoNumberFromInput" class="col-sm-2 control-label">Номера:</label>
					<div class="col-sm-5">
						<input id="filterBsoNumberFromInput" name="filter.numberFrom" type="text" class="form-control" placeholder="Номер с"/>
					</div>
					<div class="col-sm-5">
						<input id="filterBsoNumberToInput" name="filter.numberTo" type="text" class="form-control" placeholder="Номер по"/>
					</div>
				</div>
				<div class="form-group">
					<label for="filterBsoInsuredInput" class="col-sm-2 control-label">Страхователь:</label>
					<div class="col-sm-10">
						<input id="filterBsoInsuredInput" name="filter.insured" type="text" class="form-control" placeholder="Страхователь"/>
					</div>
				</div>
				<div class="form-group">
					<label for="filterBsoPremiumFromInput" class="col-sm-2 control-label">Страховая премия:</label>
					<div class="col-sm-5">
						<input id="filterBsoPremiumFromInput" name="filter.premiumFrom" type="text" class="form-control" placeholder="Премия от"/>
					</div>
					<div class="col-sm-5">
						<input id="filterBsoPremiumToInput" name="filter.premiumTo" type="text" class="form-control" placeholder="Премия до"/>
					</div>
				</div>
				<div class="form-group">
					<label for="filterBsoNodesInput" class="col-sm-2 control-label">Узлы:</label>
					<div class="col-sm-10">
						<select id="filterBsoNodesInput" name="filter.nodes" class="form-control" multiple>
							<c:forEach var="node" items="${ nodes }">
								<option value="${ node.id }">${ node.name }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="filterBsoUnitsInput" class="col-sm-2 control-label">Подразделения:</label>
					<div class="col-sm-10">
						<select id="filterBsoUnitsInput" name="filter.units" class="form-control" multiple>
							<c:forEach var="unit" items="${ units }">
								<option value="${ unit.id }">${ unit.name }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="filterBsoCreatedFromInput" class="col-sm-2 control-label">Дата создания:</label>
					<div class="col-sm-5">
						<div id="filterBsoCreatedFromInput" class='input-group date'>
							<input name="filter.createdFrom" type="text" class="form-control" placeholder="Создан с"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
					<div class="col-sm-5">
						<div id="filterBsoCreatedToInput" class='input-group date'>
							<input name="filter.createdTo" type="text" class="form-control" placeholder="Создан по"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="filterBsoIssuedFromInput" class="col-sm-2 control-label">Дата выдачи:</label>
					<div class="col-sm-5">
						<div id="filterBsoIssuedFromInput" class='input-group date'>
							<input name="filter.issuedFrom" type="text" class="form-control" placeholder="Выдан с"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
					<div class="col-sm-5">
						<div id="filterBsoIssuedToInput" class='input-group date'>
							<input name="filter.issuedTo" type="text" class="form-control" placeholder="Выдан по"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="filterBsoCorruptedFromInput" class="col-sm-2 control-label">Дата отметки испорченным:</label>
					<div class="col-sm-5">
						<div id="filterBsoCorruptedFromInput" class='input-group date'>
							<input name="filter.corruptedFrom" type="text" class="form-control" placeholder="Испорчен с"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
					<div class="col-sm-5">
						<div id="filterBsoCorruptedToInput" class='input-group date'>
							<input name="filter.corruptedTo" type="text" class="form-control" placeholder="Испорчен по"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="filterBsoRegisteredFromInput" class="col-sm-2 control-label">Дата регистрации:</label>
					<div class="col-sm-5">
						<div id="filterBsoRegisteredFromInput" class='input-group date'>
							<input name="filter.registeredFrom" type="text" class="form-control" placeholder="Зарегистрирован с"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
					<div class="col-sm-5">
						<div id="filterBsoRegisteredToInput" class='input-group date'>
							<input name="filter.registeredTo" type="text" class="form-control" placeholder="Зарегистрирован по"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-2">
						<button id="filterBsoSubmitButton" type="submit" class="btn btn-primary pull-right">Применить</button>
					</div>
					<div class="col-sm-10">
						<button id="filterBsoClearButton" type="reset" class="btn btn-default">Очистить</button>
					</div>
				</div>
			</form>
    	</div>
    </div>

	<div id="exportBsoModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="exportBsoForm" method="post" action="<c:url value='/exportBsoJson.action'/>">
					<div class="modal-header alert alert-warning">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title">Экспорт БСО в Excel</h4>
					</div>
					<div id="exportBsoModalBody" class="modal-body">
						Экспортировать БСО в Excel?
					</div>
					<div class="modal-footer">
						<button id="closeBsoButton" type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
						<button id="exportBsoButton" type="submit" class="btn btn-primary">Экспорт</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div id="toolbar">
		<div id="actToolbar" class="btn-group pull-left">
			<c:forEach var="type" items="${ types }">
				<button type="button" class="btn btn-default" data-toggle="tooltip" data-placement="top" data-type-idx="${ type.idx }" title="${ type.fullName }" value="${ type.id }">
					<c:if test="${ type.idx == 'input' }">
						<i class="glyphicon glyphicon-plus"></i>
					</c:if>
					<c:if test="${ type.idx == 'transfer' }">
						<i class="glyphicon glyphicon-transfer"></i>
					</c:if>
					<c:if test="${ type.idx == 'output' }">
						<i class="glyphicon glyphicon-remove"></i>
					</c:if>
					${ type.shortName }
				</button>
			</c:forEach>
	    </div>
	    <div class="btn-group" style="width: 50px;"></div>
		<div id="actionToolbar" class="btn-group pull-right">
			<c:forEach var="action" items="${ actions }">
				<button type="button" class="btn btn-default" data-toggle="tooltip" data-placement="top" data-action-idx="${ action.idx }" title="${ action.fullName }" value="${ action.id }">
					<c:if test="${ action.idx == 'issue' }">
						<i class="glyphicon glyphicon-shopping-cart"></i>
					</c:if>
					<c:if test="${ action.idx == 'corrupt' }">
						<i class="glyphicon glyphicon-trash"></i>
					</c:if>
					<c:if test="${ action.idx == 'register' }">
						<i class="glyphicon glyphicon-share-alt"></i>
					</c:if>
					${ action.shortName }
				</button>
			</c:forEach>
	    </div>
    </div>
	<table id="bsosTable"></table>
</c:param>
</c:import>
