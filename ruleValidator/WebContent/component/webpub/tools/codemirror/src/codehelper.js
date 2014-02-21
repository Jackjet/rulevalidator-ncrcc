seajs
		.use(
				["jquery",
						"/web/component/webpub/tools/codemirror/src/mode/javascript/javascript"],
				function($, CodeMirror) {
					$(function() {
								$("textarea.js").each(function() {
									var editor = CodeMirror.fromTextArea(this, {
												lineNumbers : true,
												matchBrackets : true,
												readOnly:true,
												theme:"rubyblue"
											});
								})
							})
				})