<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<c:import url="/WEB-INF/jsp/main.jsp">
<c:param name="link">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-table.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-multiselect.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-datetimepicker.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/journal/archived.css'/>"/>
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
	<script type="application/javascript" src="<c:url value='/js/journal/archived.js'/>"></script>
</c:param>
<c:param name="content">
	<h2>Журнал БСО (архив)</h2>
	<hr>

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
						<h4 class="modal-title">Экспорт архива БСО в Excel</h4>
					</div>
					<div id="exportBsoModalBody" class="modal-body">
						Экспортировать архив БСО в Excel?
					</div>
					<div class="modal-footer">
						<button id="closeBsoButton" type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
						<button id="exportBsoButton" type="submit" class="btn btn-primary">Экспорт</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<table id="bsosTable"></table>
</c:param>
</c:import>
