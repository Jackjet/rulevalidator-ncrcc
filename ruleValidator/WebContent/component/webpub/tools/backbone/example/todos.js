define(function(require, exports, module) {
			require("./todos.css");
			$ = require("jquery");
			var Backbone = require("tools/backbone/src/backbone");
			var _ = require('../src/underscore');
			// An example Backbone application contributed by
			// [J茅r么me Gravel-Niquet](http://jgn.me/). This demo uses a simple
			// [LocalStorage adapter](backbone-localstorage.js)
			// to persist Backbone models within your browser.

			// Load the application once the DOM is ready, using `jQuery.ready`:
			$(function() {
						// A simple module to replace `Backbone.sync` with
						// *localStorage*-based
						// persistence. Models are given GUIDS, and saved into a
						// JSON
						// object. Simple
						// as that.

						// Generate four random hex digits.
						function S4() {
							return (((1 + Math.random()) * 0x10000) | 0)
									.toString(16).substring(1);
						};

						// Generate a pseudo-GUID by concatenating random
						// hexadecimal.
						function guid() {
							return (S4() + S4() + "-" + S4() + "-" + S4() + "-"
									+ S4() + "-" + S4() + S4() + S4());
						};

						// Our Store is represented by a single JS object in
						// *localStorage*. Create it
						// with a meaningful name, like the name you'd give a
						// table.
						var Store = function(name) {
							this.name = name;
							var store = localStorage.getItem(this.name);
							this.data = (store && JSON.parse(store)) || {};
						};

						_.extend(Store.prototype, {

									// Save the current state of the **Store**
									// to
									// *localStorage*.
									save : function() {
										localStorage.setItem(this.name, JSON
														.stringify(this.data));
									},

									// Add a model, giving it a
									// (hopefully)-unique GUID,
									// if it doesn't already
									// have an id of it's own.
									create : function(model) {
										if (!model.id)
											model.id = model.attributes.id = guid();
										this.data[model.id] = model;
										this.save();
										return model;
									},

									// Update a model by replacing its copy in
									// `this.data`.
									update : function(model) {
										this.data[model.id] = model;
										this.save();
										return model;
									},

									// Retrieve a model from `this.data` by id.
									find : function(model) {
										return this.data[model.id];
									},

									// Return the array of all models currently
									// in
									// storage.
									findAll : function() {
										return _.values(this.data);
									},

									// Delete a model from `this.data`,
									// returning it.
									destroy : function(model) {
										delete this.data[model.id];
										this.save();
										return model;
									}

								});

						// Override `Backbone.sync` to use delegate to the model
						// or
						// collection's
						// *localStorage* property, which should be an instance
						// of
						// `Store`.
						Backbone.sync = function(method, model, options) {

							var resp;
							var store = model.localStorage
									|| model.collection.localStorage;

							switch (method) {
								case "read" :
									resp = model.id ? store.find(model) : store
											.findAll();
									break;
								case "create" :
									resp = store.create(model);
									break;
								case "update" :
									resp = store.update(model);
									break;
								case "delete" :
									resp = store.destroy(model);
									break;
							}

							if (resp) {
								options.success(resp);
							} else {
								options.error("Record not found");
							}
						};
						// Todo Model
						// ----------

						// Our basic **Todo** model has `title`, `order`, and
						// `done` attributes.
						var Todo = Backbone.Model.extend({

									// Default attributes for the todo item.
									defaults : function() {
										return {
											title : "empty todo...",
											order : Todos.nextOrder(),
											done : false
										};
									},

									// Ensure that each todo created has
									// `title`.
									initialize : function() {
										if (!this.get("title")) {
											this.set({
														"title" : this.defaults.title
													});
										}
									},

									// Toggle the `done` state of this todo
									// item.
									toggle : function() {
										this.save({
													done : !this.get("done")
												});
									},

									// Remove this Todo from *localStorage* and
									// delete its view.
									clear : function() {
										this.destroy();
									}

								});

						// Todo Collection
						// ---------------

						// The collection of todos is backed by *localStorage*
						// instead of a remote
						// server.
						var TodoList = Backbone.Collection.extend({

									// Reference to this collection's model.
									model : Todo,

									// Save all of the todo items under the
									// `"todos"` namespace.
									localStorage : new Store("todos-backbone"),

									// Filter down the list of all todo items
									// that are finished.
									done : function() {
										return this.filter(function(todo) {
													return todo.get('done');
												});
									},

									// Filter down the list to only todo items
									// that are still not finished.
									remaining : function() {
										return this.without.apply(this, this
														.done());
									},

									// We keep the Todos in sequential order,
									// despite being saved by unordered
									// GUID in the database. This generates the
									// next order number for new items.
									nextOrder : function() {
										if (!this.length)
											return 1;
										return this.last().get('order') + 1;
									},

									// Todos are sorted by their original
									// insertion order.
									comparator : function(todo) {
										return todo.get('order');
									}

								});

						// Create our global collection of **Todos**.
						var Todos = new TodoList;

						// Todo Item View
						// --------------

						// The DOM element for a todo item...
						var TodoView = Backbone.View.extend({

									// ... is a list tag.
									tagName : "li",

									// Cache the template function for a single
									// item.
									template : _.template($('#item-template')
											.html()),

									// The DOM events specific to an item.
									events : {
										"click .toggle" : "toggleDone",
										"dblclick .view" : "edit",
										"click a.destroy" : "clear",
										"keypress .edit" : "updateOnEnter",
										"blur .edit" : "close"
									},

									// The TodoView listens for changes to its
									// model, re-rendering. Since there's
									// a one-to-one correspondence between a
									// **Todo** and a **TodoView** in this
									// app, we set a direct reference on the
									// model for convenience.
									initialize : function() {
										this.model.bind('change', this.render,
												this);
										this.model.bind('destroy', this.remove,
												this);
									},

									// Re-render the titles of the todo item.
									render : function() {
										this.$el.html(this.template(this.model
												.toJSON()));
										this.$el.toggleClass('done', this.model
														.get('done'));
										this.input = this.$('.edit');
										return this;
									},

									// Toggle the `"done"` state of the model.
									toggleDone : function() {
										this.model.toggle();
									},

									// Switch this view into `"editing"` mode,
									// displaying the input field.
									edit : function() {
										this.$el.addClass("editing");
										this.input.focus();
									},

									// Close the `"editing"` mode, saving
									// changes to the todo.
									close : function() {
										var value = this.input.val();
										if (!value)
											this.clear();
										this.model.save({
													title : value
												});
										this.$el.removeClass("editing");
									},

									// If you hit `enter`, we're through editing
									// the item.
									updateOnEnter : function(e) {
										if (e.keyCode == 13)
											this.close();
									},

									// Remove the item, destroy the model.
									clear : function() {
										this.model.clear();
									}

								});

						// The Application
						// ---------------

						// Our overall **AppView** is the top-level piece of UI.
						var AppView = Backbone.View.extend({

									// Instead of generating a new element, bind
									// to the existing skeleton of
									// the App already present in the HTML.
									el : $("#todoapp"),

									// Our template for the line of statistics
									// at the bottom of the app.
									statsTemplate : _
											.template($('#stats-template')
													.html()),

									// Delegated events for creating new items,
									// and clearing completed ones.
									events : {
										"keypress #new-todo" : "createOnEnter",
										"click #clear-completed" : "clearCompleted",
										"click #toggle-all" : "toggleAllComplete"
									},

									// At initialization we bind to the relevant
									// events on the `Todos`
									// collection, when items are added or
									// changed. Kick things off by
									// loading any preexisting todos that might
									// be saved in *localStorage*.
									initialize : function() {

										this.input = this.$("#new-todo");
										this.allCheckbox = this
												.$("#toggle-all")[0];

										Todos.bind('add', this.addOne, this);
										Todos.bind('reset', this.addAll, this);
										Todos.bind('all', this.render, this);

										this.footer = this.$('footer');
										this.main = $('#main');

										Todos.fetch();
									},

									// Re-rendering the App just means
									// refreshing the statistics -- the rest
									// of the app doesn't change.
									render : function() {
										var done = Todos.done().length;
										var remaining = Todos.remaining().length;

										if (Todos.length) {
											this.main.show();
											this.footer.show();
											this.footer.html(this
													.statsTemplate({
																done : done,
																remaining : remaining
															}));
										} else {
											this.main.hide();
											this.footer.hide();
										}

										this.allCheckbox.checked = !remaining;
									},

									// Add a single todo item to the list by
									// creating a view for it, and
									// appending its element to the `<ul>`.
									addOne : function(todo) {
										var view = new TodoView({
													model : todo
												});
										this.$("#todo-list").append(view
												.render().el);
									},

									// Add all items in the **Todos** collection
									// at once.
									addAll : function() {
										Todos.each(this.addOne);
									},

									// If you hit return in the main input
									// field, create new **Todo** model,
									// persisting it to *localStorage*.
									createOnEnter : function(e) {
										if (e.keyCode != 13)
											return;
										if (!this.input.val())
											return;

										Todos.create({
													title : this.input.val()
												});
										this.input.val('');
									},

									// Clear all done todo items, destroying
									// their models.
									clearCompleted : function() {
										_.each(Todos.done(), function(todo) {
													todo.clear();
												});
										return false;
									},

									toggleAllComplete : function() {
										var done = this.allCheckbox.checked;
										Todos.each(function(todo) {
													todo.save({
																'done' : done
															});
												});
									}

								});

						// Finally, we kick things off by creating the **App**.
						var App = new AppView;

					});
		});
