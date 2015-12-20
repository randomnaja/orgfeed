<!DOCTYPE html PUBLIC
        "-//W3C//DTD XHTML 1.1 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Action A</title>

    <script>
        APP.namespace('actions', {
            list: '${namespaceUrl}/list.html',
            fetchPdf: '${namespaceUrl}/fetchPdf.html',
            uploadFile : '${namespaceUrl}/uploadFile.html',
            listRemoteApi : '${namespaceUrl}/listRemoteApi.html',
            listBranches : '${namespaceUrl}/listBranches.html',
            getAllAgents : '${namespaceUrl}/getAllAgents.html',
            submitJobStep1 : '${namespaceUrl}/submitJobStep1.html',
            poolingJobStep1 : '${namespaceUrl}/poolingJobStep1.html',
            searchNBSFiles : '${namespaceUrl}/searchNBSFiles.html',
            submitMapParam : '${namespaceUrl}/submitMapParam.html',
            changeMode : '${namespaceUrl}/changeMode.html',
            inquiryBizpayment : '${namespaceUrl}/inquiryBizpayment.html',
            inquiryBizpaymentAll : '${namespaceUrl}/inquiryBizpaymentAll.html',
            inquiryIndPaymentData : '${namespaceUrl}/inquiryIndPaymentData.html',
            fetchReceiptn : '${namespaceUrl}/fetchReceiptn.html',
            downloadLargeFile : '${namespaceUrl}/downloadLargeFile.html',
            getAllPDBasicPlanPA : '${namespaceUrl}/getAllPDBasicPlanPA.html',
            getAllPDCommission : '${namespaceUrl}/getAllPDCommission.html'
        });
    </script>

    <script>
        (function() {
            APP.namespace('request.parameters', {});
        })();
    </script>

    <script type="text/javascript">
        (function() {
            APP.namespace('request.parameters', {});

            document.on('layout:init', function(event) {

            });// end layout:init

            document.on('dom:loaded', function(event) {
                document.getElementById("testFormUpload").onsubmit = function() {
                    document.getElementById("testFormUpload").target = "upload_target";
                    document.getElementById("upload_target").onload = function() {
                        //var ret = frames['upload_target'].document.getElementsByTagName("body")[0].innerHTML;
                        var ret = frames['upload_target'].document.getElementsByTagName("pre")[0].innerHTML;
//                    var data = eval("("+ret+")"); //Parse JSON // Read the below explanations before passing judgment on me
                        var data = eval("("+ret+")");
                        console.log(data);
                        alert('found data : ' + JSON.stringify(data, undefined, 4));
                        //console.log(frames['upload_target'].document.getElementsByTagName("pre")[0].innerHTML);
//                    if(data.success) { //This part happens when the image gets uploaded.
//                        document.getElementById("image_details").innerHTML = "<img src='image_uploads/" + data.file_name + "' /><br />Size: " + data.size + " KB";
//                    }
//                    else if(data.failure) { //Upload failed - show user the reason.
//                        alert("Upload Failed: " + data.failure);
//                    }
                    };
                    //This function should be called when the iframe has completed loading
                    //That will happen when the file is completely uploaded and the server has returned the data we need.
                }
            });

            document.on('download:click', function(){
                window.open(APP.actions.fetchPdf, 'Fetch PDF', "height=400,width=800");
            });

            document.on('call:ajax', function(){
                document.fire('state:busy');
                new Ajax.Request(APP.actions.list, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            //APP.commands.reloadPage();
                        });
                    })
                });
            });

            document.on('call:nbsfile', function(){
                document.fire('state:busy');
                new Ajax.Request(APP.actions.listRemoteApi, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            //APP.commands.reloadPage();
                        });
                    })
                });
            });

            document.on('list:branches', function() {
                document.fire('state:busy');
                new Ajax.Request(APP.actions.listBranches, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            var sel = $$('#branches')[0];
                            sel.innerHTML = '';
                            json.data.branches.forEach(function(ele,idx){
                                sel.appendChild(new Element('option', {value:ele.brnNo})
                                        .update(ele.brnNo + " : " + ele.brnName)
                                )
                            });
                        });
                    })
                });
            });

            document.on('submit:jobStep1', function(){
                new Ajax.Request(APP.actions.submitJobStep1, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            $$('#jobResult')[0].update('Job id = ' + json.data);
                            setTimeout(function(){
                                document.fire('pooling:jobStep1');
                            }, 3000);
                        });
                    })
                });
            });

            document.on('pooling:jobStep1', function() {
                new Ajax.Request(APP.actions.poolingJobStep1, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            $$('#jobResult')[0].update(
                                    'Current state : ' + json.data.currentState
                                    + ' ::: ' + 'Current Message : ' + json.data.currentMessage
                                    + ' ::: ' + 'Processed Item : ' + json.data.processedItem
                            );

                            if (json.data.currentState == 'DONE') {
                                //TODO setTimeout here is NOT practical, just for DEMO only
                                setTimeout(function(){
                                    //document.fire('');
                                    console.log('TODO : fire job step 2');
                                }, 3000);
                            } else {
                                //TODO setTimeout here is NOT practical, just for DEMO only
                                setTimeout(function(){
                                    document.fire('pooling:jobStep1');
                                }, 3000);
                            }
                        });
                    })
                });
            });

            document.on('nbs:search', function(){
                document.fire('state:busy');
                new Ajax.Request(APP.actions.searchNBSFiles, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            $$('#searchNBSFilesResult')[0].update(json.data);
                        });
                    })
                });
            });

            document.on('list:allAgents', function() {
                document.fire('state:busy');
                new Ajax.Request(APP.actions.getAllAgents, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            var sel = $$('#allAgentResult')[0];
                            sel.innerHTML = '';
                            var jsonText = JSON.stringify(json.data, undefined, 4);
                            sel.update(jsonText);
                        });
                    })
                });
            });

            document.on('submit:mapParam', function() {
                document.fire('state:busy');
                var param = {};
                param['rowData.data.keyA'] = 'value A';
                param['rowData.data.keyB'] = 'value B';
                param['rowData.data.keyC'] = null;
                param['rowData.data.keyD'] = '';
                param['rowData.dataWithInt.keyA'] = 1;
                param['rowData.dataWithInt.keyB'] = 2;
                param['rowData.dataWithInt.keyC'] = 3;
                param['rowData.dataWithInt.keyD'] = -1;
                param['rowData.dataWithDouble.keyA'] = 0.1;
                param['rowData.dataWithDouble.keyB'] = 2.3;
                param['rowData.dataWithDouble.keyC'] = null;
                param['rowData.dataWithDouble.keyD'] = '';
                param['integerValue'] = 1;
                param['doubleValue'] = 0.9;
                param['stringValue'] = 'my string';
                param['mapValue.keyA'] = 'Value A';
                param['mapValue.keyB'] = 'Value B';

                var expectedJsonRes = '{"integerValue":1,"stringValue":"my string","doubleValue":0.9,"rowData":{"data":{"keyA":"value A","keyB":"value B","keyC":"","keyD":""},"dataWithDouble":{"keyA":0.1,"keyB":2.3,"keyC":0,"keyD":0},"dataWithInt":{"keyA":1,"keyB":2,"keyC":3,"keyD":-1}},"mapValue":{"keyA":"Value A","keyB":"Value B"}}';

                new Ajax.Request(APP.actions.submitMapParam, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    parameters : Object.toQueryString(param),
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            var unformatted = JSON.stringify(json.data);
                            console.log('unformatted result:' + unformatted);
                            var jsonText = JSON.stringify(json.data, undefined, 4);
                            console.log('pretty result : ' + jsonText);
                            alert(jsonText);

                            if (unformatted != expectedJsonRes) {
                                alert('Unexpected result is found');
                            }
                        });
                    })
                });
            });

            document.on('list:changemode', function(){
                document.fire('state:busy');
                var policyNo = $$('#policyNoOfChangeMode')[0].value;
                new Ajax.Request(APP.actions.changeMode, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    parameters : Object.toQueryString({'policyNo' : policyNo}),
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            var jsonText = JSON.stringify(json.data, undefined, 4);
                            $$('#listChangeModeResult')[0].update(jsonText);
                        });
                    })
                });
            });

            document.on('send:mail', function(){
                document.fire('state:busy');
                var mailTo = $$('#mailTo')[0].value;
                new Ajax.Request(APP.actions.sendMail, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    parameters : Object.toQueryString({'mailTo' : mailTo}),
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            alert('Sent');
                        });
                    })
                });
            });

            document.on('inquiry:bizpayment', function(){
                document.fire('state:busy');
                var br = $$('#bizpaymentBranchCode')[0].value;
                new Ajax.Request(APP.actions.inquiryBizpayment, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    parameters : Object.toQueryString({'branchNo' : br}),
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            var jsonText = JSON.stringify(json.data, undefined, 4);
                            $$('#bizpaymentByBranchCodeResult')[0].update(jsonText);
                        });
                    })
                });
            });

            document.on('inquiry:bizpaymentall', function(){
                document.fire('state:busy');
                new Ajax.Request(APP.actions.inquiryBizpayment, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            var jsonText = JSON.stringify(json.data, undefined, 4);
                            $$('#bizpaymentAllResult')[0].update(jsonText);
                        });
                    })
                });
            });

            document.on('inquiry:bizpaymentindpaymentdata', function(){
                document.fire('state:busy');
                new Ajax.Request(APP.actions.inquiryIndPaymentData, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            var jsonText = JSON.stringify(json.data, undefined, 4);
                            $$('#bizpaymentIndPaymentResult')[0].update(jsonText);
                        });
                    })
                });
            });

            document.on('fetch:receiptn', function(){
                document.fire('state:busy');
                new Ajax.Request(APP.actions.fetchReceiptn, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            var jsonText = JSON.stringify(json.data, undefined, 4);
                            $$('#receiptnResult')[0].update(jsonText);
                        });
                    })
                });
            });

            document.on('download:largefile', function(){
                window.location.href = APP.actions.downloadLargeFile;
            });

            document.on('list:PDBasicPlanPA', function(){
                document.fire('state:busy');
                new Ajax.Request(APP.actions.getAllPDBasicPlanPA, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            var jsonText = JSON.stringify(json.data, undefined, 4);
                            $$('#PDBasicPlanPAResult')[0].update(jsonText);
                        });
                    })
                });
            });

            document.on('list:PDCommission', function(){
                document.fire('state:busy');
                new Ajax.Request(APP.actions.getAllPDCommission, {
                    method : 'POST',
                    asynchronous : true,
                    encoding : 'UTF-8',
                    evalJSON : true,
                    onSuccess : (function(transport) {
                        APP.response.handlers.success(transport, function(json){
                            var jsonText = JSON.stringify(json.data, undefined, 4);
                            $$('#PDCommissionResult')[0].update(jsonText);
                        });
                    })
                });
            });

        })();
    </script>

    <link href="${staticResourceContextPath}/css/forjustthisapp.css" rel="stylesheet" type="text/css">

    <style>
        .div-inline {
            display: inline-block;
            border-bottom: 4px solid white;
            border-left: 4px solid white;
            margin: 10px;
            padding: 10px;
        }
        #jobResult {
            color: gold;
            font-weight: bolder;
        }
    </style>

</head>

<body>
<div class="layout-module-row height:20%">
    <div class="criteria layout-module">
        <div class="layout-m-hd"></div>
        <div class="layout-m-bd">
            <p>
                <button onclick="javascript:document.fire('call:ajax')" class="yui3-button">
                    Calling AJAX
                </button>
                <button onclick="javascript:document.fire('call:nbsfile')" class="yui3-button">
                    Call NBS File
                </button>
                <button onclick="javascript:document.fire('download:click')" class="yui3-button">
                    Open Jasper Report (PDF)
                </button>
            </p>

        </div>
        <div class="layout-m-ft"></div>
    </div>
</div>
<div class="layout-module-row">
    <div class="layout-module width:80%">
        <div class="layout-m-hd"></div>
        <div class="layout-m-bd">
            <div class="div-inline">
                <span>List branches (NBS Service)</span>
                <button onclick="javascript:document.fire('list:branches')" class="yui3-button">List Branches</button>
                <select id="branches"></select>
            </div>
            <div class="div-inline">
                <span>Job executor</span>
                <button onclick="javascript:document.fire('submit:jobStep1')">Start step1</button>
                Result ->
                <span id="jobResult">

                </span>
            </div>
            <div class="div-inline">
                <span>Search NBS Files</span>
                <button onclick="javascript:document.fire('nbs:search')">SearchNBSFiles</button>
                <span id="searchNBSFilesResult">

                </span>
            </div>
            <div class="div-inline">
                <button onclick="javascript:document.fire('submit:mapParam')">Submit Map Param</button>
            </div>
            <div class="div-inline">
                <input type="text" id="policyNoOfChangeMode" />
                <button onclick="javascript:document.fire('list:changemode')">List Change Mode</button>
                <pre id="listChangeModeResult">

                </pre>
            </div>
            <div class="div-inline">
                Mail to :
                <input type="text" id="mailTo" />
                <button onclick="javascript:document.fire('send:mail')">Send</button>
            </div>
            <div class="div-inline">
                <input type="text" id="bizpaymentBranchCode" />
                <button onclick="javascript:document.fire('inquiry:bizpayment')">Inquiry Bizpayment Customer by Branch Code</button>
                <pre id="bizpaymentByBranchCodeResult">

                </pre>
            </div>
            <div class="div-inline">
                <button onclick="javascript:document.fire('inquiry:bizpaymentall')">Inquiry All Bizpayment Customer</button>
                <pre id="bizpaymentAllResult">

                </pre>
            </div>
            <div class="div-inline">
                <button onclick="javascript:document.fire('inquiry:bizpaymentindpaymentdata')">Inquiry Bizpayment IND Payment</button>
                <pre id="bizpaymentIndPaymentResult">

                </pre>
            </div>
            <div class="div-inline">
                <button onclick="javascript:document.fire('fetch:receiptn')">Fetch Receiptn</button>
                <pre id="receiptnResult">

                </pre>
            </div>
            <div class="div-inline">
                <button onclick="javascript:document.fire('list:PDBasicPlanPA')">List PDBasicPlanPA</button>
                <pre id="PDBasicPlanPAResult">

                </pre>
            </div>
            <div class="div-inline">
                <button onclick="javascript:document.fire('list:PDCommission')">List PDCommission</button>
                <pre id="PDCommissionResult">

                </pre>
            </div>
            <div class="div-inline">
                <button onclick="javascript:document.fire('download:largefile')">Download large file</button>
            </div>
            <p>
                <ul>
                    <li>&#36;{namespaceUrl} = ${namespaceUrl}</li>
                    <li>&#36;{jsYuiHome} = ${jsYuiHome}</li>
                    <li>&#36;{jsYuiGallery} = ${jsYuiGallery}</li>
                    <li>&#36;{jsPrototypeHome} = ${jsPrototypeHome}</li>
                    <li>&#36;{staticResourceContextPath} = ${staticResourceContextPath}</li>
                    <li>i18n Label : <@msg.message "label.text"/></li>
                    <li>i18n Thai : <@msg.message "label.thai"/></li>
                </ul>
            </p>
            <form id="testFormUpload" method="POST"
                  action="${namespaceUrl}/uploadFile.html"
                  enctype="multipart/form-data">
                <fieldset>
                    <legend>Upload Form</legend>
                    <label for="anyinputvalue">Input Value : </label>
                    <input type="text" name="anyinputvalue" value="Any Value" />
                    <br/>
                    <label>Multiple File with same Input Name : </label>
                    <input type="file" name="testFile" />
                    <input type="file" name="testFile" />
                    <br/>
                    <label>Single File : </label>
                    <input type="file" name="testSingleFile" />
                    <br/>
                    <input type="submit" value="Upload" />
                    <br/>
                </fieldset>
                <iframe id="upload_target" name="upload_target"
                        src="" style="width:0px;height:0px;border:0px solid;"></iframe>
            </form>
        </div>
        <div class="layout-m-ft"></div>
    </div>
    <div class="layout-module">
        <div class="layout-m-hd"></div>
        <div class="layout-m-bd">
            <p>
                <span>List all agents</span>
                <button onclick="javascript:document.fire('list:allAgents')">List all Agents</button>
                <pre id="allAgentResult">
                </pre>
            </p>
        </div>
        <div class="layout-m-ft"></div>
    </div>
</div>

</body>

</html>