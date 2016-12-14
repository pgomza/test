webpackJsonp([1],{

/***/ 315:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _getPrototypeOf = __webpack_require__(316);

	var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

	var _classCallCheck2 = __webpack_require__(321);

	var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

	var _createClass2 = __webpack_require__(322);

	var _createClass3 = _interopRequireDefault(_createClass2);

	var _possibleConstructorReturn2 = __webpack_require__(326);

	var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);

	var _inherits2 = __webpack_require__(360);

	var _inherits3 = _interopRequireDefault(_inherits2);

	var _react = __webpack_require__(24);

	var _react2 = _interopRequireDefault(_react);

	var _reactRedux = __webpack_require__(368);

	var _Login = __webpack_require__(375);

	var _Login2 = _interopRequireDefault(_Login);

	var _loginService = __webpack_require__(384);

	var loginService = _interopRequireWildcard(_loginService);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var LoginContainer = function (_Component) {
	  (0, _inherits3.default)(LoginContainer, _Component);

	  function LoginContainer() {
	    var _ref;

	    var _temp, _this, _ret;

	    (0, _classCallCheck3.default)(this, LoginContainer);

	    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
	      args[_key] = arguments[_key];
	    }

	    return _ret = (_temp = (_this = (0, _possibleConstructorReturn3.default)(this, (_ref = LoginContainer.__proto__ || (0, _getPrototypeOf2.default)(LoginContainer)).call.apply(_ref, [this].concat(args))), _this), _this.handleFormSubmit = function (model) {
	      _this.props.dispatch(loginService.getNewLoginAction(model.login, model.password));
	    }, _temp), (0, _possibleConstructorReturn3.default)(_this, _ret);
	  }

	  (0, _createClass3.default)(LoginContainer, [{
	    key: 'render',
	    value: function render() {
	      var login = this.props.login;


	      return _react2.default.createElement(_Login2.default, { loginStatus: login.status, handleFormSubmit: this.handleFormSubmit });
	    }
	  }]);
	  return LoginContainer;
	}(_react.Component);

	var mapStateToProps = function mapStateToProps(state) {
	  return {
	    login: state.login
	  };
	};

	LoginContainer = (0, _reactRedux.connect)(mapStateToProps, null)(LoginContainer);
	exports.default = LoginContainer;

/***/ },

/***/ 375:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _extends2 = __webpack_require__(275);

	var _extends3 = _interopRequireDefault(_extends2);

	var _getPrototypeOf = __webpack_require__(316);

	var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

	var _classCallCheck2 = __webpack_require__(321);

	var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

	var _createClass2 = __webpack_require__(322);

	var _createClass3 = _interopRequireDefault(_createClass2);

	var _possibleConstructorReturn2 = __webpack_require__(326);

	var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);

	var _inherits2 = __webpack_require__(360);

	var _inherits3 = _interopRequireDefault(_inherits2);

	var _react = __webpack_require__(24);

	var _react2 = _interopRequireDefault(_react);

	var _formsyReact = __webpack_require__(376);

	var _StandardInput = __webpack_require__(383);

	var _StandardInput2 = _interopRequireDefault(_StandardInput);

	var _loginService = __webpack_require__(384);

	var loginService = _interopRequireWildcard(_loginService);

	var _jquery = __webpack_require__(389);

	var _jquery2 = _interopRequireDefault(_jquery);

	__webpack_require__(390);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var Login = function (_Component) {
	  (0, _inherits3.default)(Login, _Component);

	  function Login(props) {
	    (0, _classCallCheck3.default)(this, Login);

	    var _this = (0, _possibleConstructorReturn3.default)(this, (Login.__proto__ || (0, _getPrototypeOf2.default)(Login)).call(this, props));

	    _this.handleOnValid = function () {
	      _this.setState((0, _extends3.default)({}, _this.state, { isValid: true }));
	    };

	    _this.handleOnInvalid = function () {
	      _this.setState((0, _extends3.default)({}, _this.state, { isValid: false }));
	    };

	    _this.state = {
	      login: '',
	      password: '',
	      isValid: false
	    };
	    return _this;
	  }

	  (0, _createClass3.default)(Login, [{
	    key: 'render',
	    value: function render() {
	      var _this2 = this;

	      var _props = this.props,
	          loginStatus = _props.loginStatus,
	          handleFormSubmit = _props.handleFormSubmit;


	      var isPending = loginStatus === loginService.LOGIN_PENDING;
	      var wrongCredentials = loginStatus === loginService.LOGIN_WRONG_CREDENTIALS;
	      var unknownError = loginStatus === loginService.LOGIN_ERROR;

	      return _react2.default.createElement(
	        'div',
	        { className: 'row' },
	        _react2.default.createElement(
	          'div',
	          { className: 'col-md-4 col-md-offset-4 col-xs-8' },
	          _react2.default.createElement(
	            'div',
	            { className: 'panel panel-default login' },
	            _react2.default.createElement(
	              'div',
	              { className: 'panel-heading' },
	              _react2.default.createElement(
	                'h4',
	                null,
	                'Log in'
	              )
	            ),
	            _react2.default.createElement(
	              'div',
	              { className: 'panel-body' },
	              _react2.default.createElement(
	                _formsyReact.Form,
	                { onSubmit: handleFormSubmit,
	                  onValid: this.handleOnValid,
	                  onInvalid: this.handleOnInvalid,
	                  className: 'form-group',
	                  role: 'form',
	                  onKeyDown: function onKeyDown(e) {
	                    if (e.key === 'Enter') e.preventDefault();
	                  }
	                },
	                _react2.default.createElement(
	                  'div',
	                  { className: 'form-group' },
	                  _react2.default.createElement(
	                    'label',
	                    { htmlFor: 'login' },
	                    'Login *'
	                  ),
	                  _react2.default.createElement(_StandardInput2.default, { value: this.state.login,
	                    name: 'login',
	                    title: 'Login',
	                    placeholder: '',
	                    validations: 'isAlphanumeric,minLength:1',
	                    validationError: 'This is not a valid login',
	                    required: true
	                  })
	                ),
	                _react2.default.createElement(
	                  'div',
	                  { className: 'form-group' },
	                  _react2.default.createElement(
	                    'label',
	                    { htmlFor: 'password' },
	                    'Password *'
	                  ),
	                  _react2.default.createElement(_StandardInput2.default, { value: this.state.login,
	                    type: 'password',
	                    name: 'password',
	                    title: 'Password',
	                    placeholder: '',
	                    validations: 'isAlphanumeric,minLength:1',
	                    validationError: 'This is not a valid password',
	                    required: true
	                  })
	                ),
	                isPending && _react2.default.createElement(
	                  'div',
	                  { className: 'login-pending' },
	                  _react2.default.createElement('i', { className: 'fa fa-spinner fa-spin', 'aria-hidden': 'true' }),
	                  ' Logging in...'
	                ),
	                wrongCredentials && _react2.default.createElement(
	                  'div',
	                  { className: 'login-error' },
	                  'Wrong credentials'
	                ),
	                unknownError && _react2.default.createElement(
	                  'div',
	                  { className: 'login-error' },
	                  'An error occurred while logging in'
	                ),
	                _react2.default.createElement(
	                  'button',
	                  { type: 'submit',
	                    className: "pull-right btn btn-primary " + (this.state.isValid ? "" : "disabled"),
	                    onClick: function onClick(e) {
	                      if (!_this2.state.isValid) e.preventDefault();
	                    }
	                  },
	                  'Submit'
	                )
	              )
	            )
	          )
	        )
	      );
	    }
	  }]);
	  return Login;
	}(_react.Component);

	exports.default = Login;


	Login.propTypes = {
	  loginStatus: _react.PropTypes.string.isRequired,
	  handleFormSubmit: _react.PropTypes.func.isRequired
	};

/***/ },

/***/ 376:
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function(global) {'use strict';

	var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

	var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol ? "symbol" : typeof obj; };

	function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

	var React = global.React || __webpack_require__(24);
	var Formsy = {};
	var validationRules = __webpack_require__(377);
	var formDataToObject = __webpack_require__(378);
	var utils = __webpack_require__(379);
	var Mixin = __webpack_require__(380);
	var HOC = __webpack_require__(381);
	var Decorator = __webpack_require__(382);
	var options = {};
	var emptyArray = [];

	Formsy.Mixin = Mixin;
	Formsy.HOC = HOC;
	Formsy.Decorator = Decorator;

	Formsy.defaults = function (passedOptions) {
	  options = passedOptions;
	};

	Formsy.addValidationRule = function (name, func) {
	  validationRules[name] = func;
	};

	Formsy.Form = React.createClass({
	  displayName: 'Formsy',
	  getInitialState: function getInitialState() {
	    return {
	      isValid: true,
	      isSubmitting: false,
	      canChange: false
	    };
	  },
	  getDefaultProps: function getDefaultProps() {
	    return {
	      onSuccess: function onSuccess() {},
	      onError: function onError() {},
	      onSubmit: function onSubmit() {},
	      onValidSubmit: function onValidSubmit() {},
	      onInvalidSubmit: function onInvalidSubmit() {},
	      onValid: function onValid() {},
	      onInvalid: function onInvalid() {},
	      onChange: function onChange() {},
	      validationErrors: null,
	      preventExternalInvalidation: false
	    };
	  },

	  childContextTypes: {
	    formsy: React.PropTypes.object
	  },
	  getChildContext: function getChildContext() {
	    var _this = this;

	    return {
	      formsy: {
	        attachToForm: this.attachToForm,
	        detachFromForm: this.detachFromForm,
	        validate: this.validate,
	        isFormDisabled: this.isFormDisabled,
	        isValidValue: function isValidValue(component, value) {
	          return _this.runValidation(component, value).isValid;
	        }
	      }
	    };
	  },

	  // Add a map to store the inputs of the form, a model to store
	  // the values of the form and register child inputs
	  componentWillMount: function componentWillMount() {
	    this.inputs = [];
	  },

	  componentDidMount: function componentDidMount() {
	    this.validateForm();
	  },

	  componentWillUpdate: function componentWillUpdate() {
	    // Keep a reference to input names before form updates,
	    // to check if inputs has changed after render
	    this.prevInputNames = this.inputs.map(function (component) {
	      return component.props.name;
	    });
	  },

	  componentDidUpdate: function componentDidUpdate() {

	    if (this.props.validationErrors && _typeof(this.props.validationErrors) === 'object' && Object.keys(this.props.validationErrors).length > 0) {
	      this.setInputValidationErrors(this.props.validationErrors);
	    }

	    var newInputNames = this.inputs.map(function (component) {
	      return component.props.name;
	    });
	    if (utils.arraysDiffer(this.prevInputNames, newInputNames)) {
	      this.validateForm();
	    }
	  },

	  // Allow resetting to specified data
	  reset: function reset(data) {
	    this.setFormPristine(true);
	    this.resetModel(data);
	  },

	  // Update model, submit to url prop and send the model
	  submit: function submit(event) {

	    event && event.preventDefault();

	    // Trigger form as not pristine.
	    // If any inputs have not been touched yet this will make them dirty
	    // so validation becomes visible (if based on isPristine)
	    this.setFormPristine(false);
	    var model = this.getModel();
	    this.props.onSubmit(model, this.resetModel, this.updateInputsWithError);
	    this.state.isValid ? this.props.onValidSubmit(model, this.resetModel, this.updateInputsWithError) : this.props.onInvalidSubmit(model, this.resetModel, this.updateInputsWithError);
	  },

	  mapModel: function mapModel(model) {

	    if (this.props.mapping) {
	      return this.props.mapping(model);
	    } else {
	      return formDataToObject.toObj(Object.keys(model).reduce(function (mappedModel, key) {

	        var keyArray = key.split('.');
	        var base = mappedModel;
	        while (keyArray.length) {
	          var currentKey = keyArray.shift();
	          base = base[currentKey] = keyArray.length ? base[currentKey] || {} : model[key];
	        }

	        return mappedModel;
	      }, {}));
	    }
	  },

	  getModel: function getModel() {
	    var currentValues = this.getCurrentValues();
	    return this.mapModel(currentValues);
	  },

	  // Reset each key in the model to the original / initial / specified value
	  resetModel: function resetModel(data) {
	    this.inputs.forEach(function (component) {
	      var name = component.props.name;
	      if (data && data.hasOwnProperty(name)) {
	        component.setValue(data[name]);
	      } else {
	        component.resetValue();
	      }
	    });
	    this.validateForm();
	  },

	  setInputValidationErrors: function setInputValidationErrors(errors) {
	    this.inputs.forEach(function (component) {
	      var name = component.props.name;
	      var args = [{
	        _isValid: !(name in errors),
	        _validationError: typeof errors[name] === 'string' ? [errors[name]] : errors[name]
	      }];
	      component.setState.apply(component, args);
	    });
	  },

	  // Checks if the values have changed from their initial value
	  isChanged: function isChanged() {
	    return !utils.isSame(this.getPristineValues(), this.getCurrentValues());
	  },

	  getPristineValues: function getPristineValues() {
	    return this.inputs.reduce(function (data, component) {
	      var name = component.props.name;
	      data[name] = component.props.value;
	      return data;
	    }, {});
	  },

	  // Go through errors from server and grab the components
	  // stored in the inputs map. Change their state to invalid
	  // and set the serverError message
	  updateInputsWithError: function updateInputsWithError(errors) {
	    var _this2 = this;

	    Object.keys(errors).forEach(function (name, index) {
	      var component = utils.find(_this2.inputs, function (component) {
	        return component.props.name === name;
	      });
	      if (!component) {
	        throw new Error('You are trying to update an input that does not exist. ' + 'Verify errors object with input names. ' + JSON.stringify(errors));
	      }
	      var args = [{
	        _isValid: _this2.props.preventExternalInvalidation || false,
	        _externalError: typeof errors[name] === 'string' ? [errors[name]] : errors[name]
	      }];
	      component.setState.apply(component, args);
	    });
	  },

	  isFormDisabled: function isFormDisabled() {
	    return this.props.disabled;
	  },

	  getCurrentValues: function getCurrentValues() {
	    return this.inputs.reduce(function (data, component) {
	      var name = component.props.name;
	      data[name] = component.state._value;
	      return data;
	    }, {});
	  },

	  setFormPristine: function setFormPristine(isPristine) {
	    this.setState({
	      _formSubmitted: !isPristine
	    });

	    // Iterate through each component and set it as pristine
	    // or "dirty".
	    this.inputs.forEach(function (component, index) {
	      component.setState({
	        _formSubmitted: !isPristine,
	        _isPristine: isPristine
	      });
	    });
	  },

	  // Use the binded values and the actual input value to
	  // validate the input and set its state. Then check the
	  // state of the form itself
	  validate: function validate(component) {

	    // Trigger onChange
	    if (this.state.canChange) {
	      this.props.onChange(this.getCurrentValues(), this.isChanged());
	    }

	    var validation = this.runValidation(component);
	    // Run through the validations, split them up and call
	    // the validator IF there is a value or it is required
	    component.setState({
	      _isValid: validation.isValid,
	      _isRequired: validation.isRequired,
	      _validationError: validation.error,
	      _externalError: null
	    }, this.validateForm);
	  },

	  // Checks validation on current value or a passed value
	  runValidation: function runValidation(component, value) {

	    var currentValues = this.getCurrentValues();
	    var validationErrors = component.props.validationErrors;
	    var validationError = component.props.validationError;
	    value = arguments.length === 2 ? value : component.state._value;

	    var validationResults = this.runRules(value, currentValues, component._validations);
	    var requiredResults = this.runRules(value, currentValues, component._requiredValidations);

	    // the component defines an explicit validate function
	    if (typeof component.validate === "function") {
	      validationResults.failed = component.validate() ? [] : ['failed'];
	    }

	    var isRequired = Object.keys(component._requiredValidations).length ? !!requiredResults.success.length : false;
	    var isValid = !validationResults.failed.length && !(this.props.validationErrors && this.props.validationErrors[component.props.name]);

	    return {
	      isRequired: isRequired,
	      isValid: isRequired ? false : isValid,
	      error: function () {

	        if (isValid && !isRequired) {
	          return emptyArray;
	        }

	        if (validationResults.errors.length) {
	          return validationResults.errors;
	        }

	        if (this.props.validationErrors && this.props.validationErrors[component.props.name]) {
	          return typeof this.props.validationErrors[component.props.name] === 'string' ? [this.props.validationErrors[component.props.name]] : this.props.validationErrors[component.props.name];
	        }

	        if (isRequired) {
	          var error = validationErrors[requiredResults.success[0]];
	          return error ? [error] : null;
	        }

	        if (validationResults.failed.length) {
	          return validationResults.failed.map(function (failed) {
	            return validationErrors[failed] ? validationErrors[failed] : validationError;
	          }).filter(function (x, pos, arr) {
	            // Remove duplicates
	            return arr.indexOf(x) === pos;
	          });
	        }
	      }.call(this)
	    };
	  },

	  runRules: function runRules(value, currentValues, validations) {

	    var results = {
	      errors: [],
	      failed: [],
	      success: []
	    };
	    if (Object.keys(validations).length) {
	      Object.keys(validations).forEach(function (validationMethod) {

	        if (validationRules[validationMethod] && typeof validations[validationMethod] === 'function') {
	          throw new Error('Formsy does not allow you to override default validations: ' + validationMethod);
	        }

	        if (!validationRules[validationMethod] && typeof validations[validationMethod] !== 'function') {
	          throw new Error('Formsy does not have the validation rule: ' + validationMethod);
	        }

	        if (typeof validations[validationMethod] === 'function') {
	          var validation = validations[validationMethod](currentValues, value);
	          if (typeof validation === 'string') {
	            results.errors.push(validation);
	            results.failed.push(validationMethod);
	          } else if (!validation) {
	            results.failed.push(validationMethod);
	          }
	          return;
	        } else if (typeof validations[validationMethod] !== 'function') {
	          var validation = validationRules[validationMethod](currentValues, value, validations[validationMethod]);
	          if (typeof validation === 'string') {
	            results.errors.push(validation);
	            results.failed.push(validationMethod);
	          } else if (!validation) {
	            results.failed.push(validationMethod);
	          } else {
	            results.success.push(validationMethod);
	          }
	          return;
	        }

	        return results.success.push(validationMethod);
	      });
	    }

	    return results;
	  },

	  // Validate the form by going through all child input components
	  // and check their state
	  validateForm: function validateForm() {
	    var _this3 = this;

	    // We need a callback as we are validating all inputs again. This will
	    // run when the last component has set its state
	    var onValidationComplete = function () {
	      var allIsValid = this.inputs.every(function (component) {
	        return component.state._isValid;
	      });

	      this.setState({
	        isValid: allIsValid
	      });

	      if (allIsValid) {
	        this.props.onValid();
	      } else {
	        this.props.onInvalid();
	      }

	      // Tell the form that it can start to trigger change events
	      this.setState({
	        canChange: true
	      });
	    }.bind(this);

	    // Run validation again in case affected by other inputs. The
	    // last component validated will run the onValidationComplete callback
	    this.inputs.forEach(function (component, index) {
	      var validation = _this3.runValidation(component);
	      if (validation.isValid && component.state._externalError) {
	        validation.isValid = false;
	      }
	      component.setState({
	        _isValid: validation.isValid,
	        _isRequired: validation.isRequired,
	        _validationError: validation.error,
	        _externalError: !validation.isValid && component.state._externalError ? component.state._externalError : null
	      }, index === _this3.inputs.length - 1 ? onValidationComplete : null);
	    });

	    // If there are no inputs, set state where form is ready to trigger
	    // change event. New inputs might be added later
	    if (!this.inputs.length && this.isMounted()) {
	      this.setState({
	        canChange: true
	      });
	    }
	  },

	  // Method put on each input component to register
	  // itself to the form
	  attachToForm: function attachToForm(component) {

	    if (this.inputs.indexOf(component) === -1) {
	      this.inputs.push(component);
	    }

	    this.validate(component);
	  },

	  // Method put on each input component to unregister
	  // itself from the form
	  detachFromForm: function detachFromForm(component) {
	    var componentPos = this.inputs.indexOf(component);

	    if (componentPos !== -1) {
	      this.inputs = this.inputs.slice(0, componentPos).concat(this.inputs.slice(componentPos + 1));
	    }

	    this.validateForm();
	  },
	  render: function render() {
	    var _props = this.props;
	    var mapping = _props.mapping;
	    var validationErrors = _props.validationErrors;
	    var onSubmit = _props.onSubmit;
	    var onValid = _props.onValid;
	    var onValidSubmit = _props.onValidSubmit;
	    var onInvalid = _props.onInvalid;
	    var onInvalidSubmit = _props.onInvalidSubmit;
	    var onChange = _props.onChange;
	    var reset = _props.reset;
	    var preventExternalInvalidation = _props.preventExternalInvalidation;
	    var onSuccess = _props.onSuccess;
	    var onError = _props.onError;

	    var nonFormsyProps = _objectWithoutProperties(_props, ['mapping', 'validationErrors', 'onSubmit', 'onValid', 'onValidSubmit', 'onInvalid', 'onInvalidSubmit', 'onChange', 'reset', 'preventExternalInvalidation', 'onSuccess', 'onError']);

	    return React.createElement(
	      'form',
	      _extends({}, nonFormsyProps, { onSubmit: this.submit }),
	      this.props.children
	    );
	  }
	});

	if (!global.exports && !global.module && (!global.define || !global.define.amd)) {
	  global.Formsy = Formsy;
	}

	module.exports = Formsy;
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ },

/***/ 377:
/***/ function(module, exports) {

	'use strict';

	var _isExisty = function _isExisty(value) {
	  return value !== null && value !== undefined;
	};

	var isEmpty = function isEmpty(value) {
	  return value === '';
	};

	var validations = {
	  isDefaultRequiredValue: function isDefaultRequiredValue(values, value) {
	    return value === undefined || value === '';
	  },
	  isExisty: function isExisty(values, value) {
	    return _isExisty(value);
	  },
	  matchRegexp: function matchRegexp(values, value, regexp) {
	    return !_isExisty(value) || isEmpty(value) || regexp.test(value);
	  },
	  isUndefined: function isUndefined(values, value) {
	    return value === undefined;
	  },
	  isEmptyString: function isEmptyString(values, value) {
	    return isEmpty(value);
	  },
	  isEmail: function isEmail(values, value) {
	    return validations.matchRegexp(values, value, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i);
	  },
	  isUrl: function isUrl(values, value) {
	    return validations.matchRegexp(values, value, /^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i);
	  },
	  isTrue: function isTrue(values, value) {
	    return value === true;
	  },
	  isFalse: function isFalse(values, value) {
	    return value === false;
	  },
	  isNumeric: function isNumeric(values, value) {
	    if (typeof value === 'number') {
	      return true;
	    }
	    return validations.matchRegexp(values, value, /^[-+]?(?:\d*[.])?\d+$/);
	  },
	  isAlpha: function isAlpha(values, value) {
	    return validations.matchRegexp(values, value, /^[A-Z]+$/i);
	  },
	  isAlphanumeric: function isAlphanumeric(values, value) {
	    return validations.matchRegexp(values, value, /^[0-9A-Z]+$/i);
	  },
	  isInt: function isInt(values, value) {
	    return validations.matchRegexp(values, value, /^(?:[-+]?(?:0|[1-9]\d*))$/);
	  },
	  isFloat: function isFloat(values, value) {
	    return validations.matchRegexp(values, value, /^(?:[-+]?(?:\d+))?(?:\.\d*)?(?:[eE][\+\-]?(?:\d+))?$/);
	  },
	  isWords: function isWords(values, value) {
	    return validations.matchRegexp(values, value, /^[A-Z\s]+$/i);
	  },
	  isSpecialWords: function isSpecialWords(values, value) {
	    return validations.matchRegexp(values, value, /^[A-Z\s\u00C0-\u017F]+$/i);
	  },
	  isLength: function isLength(values, value, length) {
	    return !_isExisty(value) || isEmpty(value) || value.length === length;
	  },
	  equals: function equals(values, value, eql) {
	    return !_isExisty(value) || isEmpty(value) || value == eql;
	  },
	  equalsField: function equalsField(values, value, field) {
	    return value == values[field];
	  },
	  maxLength: function maxLength(values, value, length) {
	    return !_isExisty(value) || value.length <= length;
	  },
	  minLength: function minLength(values, value, length) {
	    return !_isExisty(value) || isEmpty(value) || value.length >= length;
	  }
	};

	module.exports = validations;

/***/ },

/***/ 378:
/***/ function(module, exports) {

	function toObj(source) {
	  return Object.keys(source).reduce(function (output, key) {
	    var parentKey = key.match(/[^\[]*/i);
	    var paths = key.match(/\[.*?\]/g) || [];
	    paths = [parentKey[0]].concat(paths).map(function (key) {
	      return key.replace(/\[|\]/g, '');
	    });
	    var currentPath = output;
	    while (paths.length) {
	      var pathKey = paths.shift();

	      if (pathKey in currentPath) {
	        currentPath = currentPath[pathKey];
	      } else {
	        currentPath[pathKey] = paths.length ? isNaN(paths[0]) ? {} : [] : source[key];
	        currentPath = currentPath[pathKey];
	      }
	    }

	    return output;
	  }, {});
	}

	function fromObj(obj) {
	  function recur(newObj, propName, currVal) {
	    if (Array.isArray(currVal) || Object.prototype.toString.call(currVal) === '[object Object]') {
	      Object.keys(currVal).forEach(function(v) {
	        recur(newObj, propName + "[" + v + "]", currVal[v]);
	      });
	      return newObj;
	    }

	    newObj[propName] = currVal;
	    return newObj;
	  }

	  var keys = Object.keys(obj);
	  return keys.reduce(function(newObj, propName) {
	    return recur(newObj, propName, obj[propName]);
	  }, {});
	}

	module.exports = {
	  fromObj: fromObj,
	  toObj: toObj
	}

/***/ },

/***/ 379:
/***/ function(module, exports) {

	'use strict';

	var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol ? "symbol" : typeof obj; };

	module.exports = {
	  arraysDiffer: function arraysDiffer(a, b) {
	    var isDifferent = false;
	    if (a.length !== b.length) {
	      isDifferent = true;
	    } else {
	      a.forEach(function (item, index) {
	        if (!this.isSame(item, b[index])) {
	          isDifferent = true;
	        }
	      }, this);
	    }
	    return isDifferent;
	  },

	  objectsDiffer: function objectsDiffer(a, b) {
	    var isDifferent = false;
	    if (Object.keys(a).length !== Object.keys(b).length) {
	      isDifferent = true;
	    } else {
	      Object.keys(a).forEach(function (key) {
	        if (!this.isSame(a[key], b[key])) {
	          isDifferent = true;
	        }
	      }, this);
	    }
	    return isDifferent;
	  },

	  isSame: function isSame(a, b) {
	    if ((typeof a === 'undefined' ? 'undefined' : _typeof(a)) !== (typeof b === 'undefined' ? 'undefined' : _typeof(b))) {
	      return false;
	    } else if (Array.isArray(a)) {
	      return !this.arraysDiffer(a, b);
	    } else if (typeof a === 'function') {
	      return a.toString() === b.toString();
	    } else if ((typeof a === 'undefined' ? 'undefined' : _typeof(a)) === 'object' && a !== null && b !== null) {
	      return !this.objectsDiffer(a, b);
	    }

	    return a === b;
	  },

	  find: function find(collection, fn) {
	    for (var i = 0, l = collection.length; i < l; i++) {
	      var item = collection[i];
	      if (fn(item)) {
	        return item;
	      }
	    }
	    return null;
	  }
	};

/***/ },

/***/ 380:
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function(global) {'use strict';

	var utils = __webpack_require__(379);
	var React = global.React || __webpack_require__(24);

	var convertValidationsToObject = function convertValidationsToObject(validations) {

	  if (typeof validations === 'string') {

	    return validations.split(/\,(?![^{\[]*[}\]])/g).reduce(function (validations, validation) {
	      var args = validation.split(':');
	      var validateMethod = args.shift();

	      args = args.map(function (arg) {
	        try {
	          return JSON.parse(arg);
	        } catch (e) {
	          return arg; // It is a string if it can not parse it
	        }
	      });

	      if (args.length > 1) {
	        throw new Error('Formsy does not support multiple args on string validations. Use object format of validations instead.');
	      }

	      validations[validateMethod] = args.length ? args[0] : true;
	      return validations;
	    }, {});
	  }

	  return validations || {};
	};

	module.exports = {
	  getInitialState: function getInitialState() {
	    return {
	      _value: this.props.value,
	      _isRequired: false,
	      _isValid: true,
	      _isPristine: true,
	      _pristineValue: this.props.value,
	      _validationError: [],
	      _externalError: null,
	      _formSubmitted: false
	    };
	  },
	  contextTypes: {
	    formsy: React.PropTypes.object // What about required?
	  },
	  getDefaultProps: function getDefaultProps() {
	    return {
	      validationError: '',
	      validationErrors: {}
	    };
	  },

	  componentWillMount: function componentWillMount() {
	    var configure = function () {
	      this.setValidations(this.props.validations, this.props.required);

	      // Pass a function instead?
	      this.context.formsy.attachToForm(this);
	      //this.props._attachToForm(this);
	    }.bind(this);

	    if (!this.props.name) {
	      throw new Error('Form Input requires a name property when used');
	    }

	    /*
	    if (!this.props._attachToForm) {
	      return setTimeout(function () {
	        if (!this.isMounted()) return;
	        if (!this.props._attachToForm) {
	          throw new Error('Form Mixin requires component to be nested in a Form');
	        }
	        configure();
	      }.bind(this), 0);
	    }
	    */
	    configure();
	  },

	  // We have to make the validate method is kept when new props are added
	  componentWillReceiveProps: function componentWillReceiveProps(nextProps) {
	    this.setValidations(nextProps.validations, nextProps.required);
	  },

	  componentDidUpdate: function componentDidUpdate(prevProps) {

	    // If the value passed has changed, set it. If value is not passed it will
	    // internally update, and this will never run
	    if (!utils.isSame(this.props.value, prevProps.value)) {
	      this.setValue(this.props.value);
	    }

	    // If validations or required is changed, run a new validation
	    if (!utils.isSame(this.props.validations, prevProps.validations) || !utils.isSame(this.props.required, prevProps.required)) {
	      this.context.formsy.validate(this);
	    }
	  },

	  // Detach it when component unmounts
	  componentWillUnmount: function componentWillUnmount() {
	    this.context.formsy.detachFromForm(this);
	    //this.props._detachFromForm(this);
	  },

	  setValidations: function setValidations(validations, required) {

	    // Add validations to the store itself as the props object can not be modified
	    this._validations = convertValidationsToObject(validations) || {};
	    this._requiredValidations = required === true ? { isDefaultRequiredValue: true } : convertValidationsToObject(required);
	  },

	  // We validate after the value has been set
	  setValue: function setValue(value) {
	    this.setState({
	      _value: value,
	      _isPristine: false
	    }, function () {
	      this.context.formsy.validate(this);
	      //this.props._validate(this);
	    }.bind(this));
	  },
	  resetValue: function resetValue() {
	    this.setState({
	      _value: this.state._pristineValue,
	      _isPristine: true
	    }, function () {
	      this.context.formsy.validate(this);
	      //this.props._validate(this);
	    });
	  },
	  getValue: function getValue() {
	    return this.state._value;
	  },
	  hasValue: function hasValue() {
	    return this.state._value !== '';
	  },
	  getErrorMessage: function getErrorMessage() {
	    var messages = this.getErrorMessages();
	    return messages.length ? messages[0] : null;
	  },
	  getErrorMessages: function getErrorMessages() {
	    return !this.isValid() || this.showRequired() ? this.state._externalError || this.state._validationError || [] : [];
	  },
	  isFormDisabled: function isFormDisabled() {
	    return this.context.formsy.isFormDisabled();
	    //return this.props._isFormDisabled();
	  },
	  isValid: function isValid() {
	    return this.state._isValid;
	  },
	  isPristine: function isPristine() {
	    return this.state._isPristine;
	  },
	  isFormSubmitted: function isFormSubmitted() {
	    return this.state._formSubmitted;
	  },
	  isRequired: function isRequired() {
	    return !!this.props.required;
	  },
	  showRequired: function showRequired() {
	    return this.state._isRequired;
	  },
	  showError: function showError() {
	    return !this.showRequired() && !this.isValid();
	  },
	  isValidValue: function isValidValue(value) {
	    return this.context.formsy.isValidValue.call(null, this, value);
	    //return this.props._isValidValue.call(null, this, value);
	  }
	};
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ },

/***/ 381:
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function(global) {'use strict';

	var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

	var React = global.React || __webpack_require__(24);
	var Mixin = __webpack_require__(380);
	module.exports = function (Component) {
	  return React.createClass({
	    displayName: 'Formsy(' + getDisplayName(Component) + ')',
	    mixins: [Mixin],
	    render: function render() {
	      return React.createElement(Component, _extends({
	        setValidations: this.setValidations,
	        setValue: this.setValue,
	        resetValue: this.resetValue,
	        getValue: this.getValue,
	        hasValue: this.hasValue,
	        getErrorMessage: this.getErrorMessage,
	        getErrorMessages: this.getErrorMessages,
	        isFormDisabled: this.isFormDisabled,
	        isValid: this.isValid,
	        isPristine: this.isPristine,
	        isFormSubmitted: this.isFormSubmitted,
	        isRequired: this.isRequired,
	        showRequired: this.showRequired,
	        showError: this.showError,
	        isValidValue: this.isValidValue
	      }, this.props));
	    }
	  });
	};

	function getDisplayName(Component) {
	  return Component.displayName || Component.name || (typeof Component === 'string' ? Component : 'Component');
	}
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ },

/***/ 382:
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function(global) {'use strict';

	var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

	var React = global.React || __webpack_require__(24);
	var Mixin = __webpack_require__(380);
	module.exports = function () {
	  return function (Component) {
	    return React.createClass({
	      mixins: [Mixin],
	      render: function render() {
	        return React.createElement(Component, _extends({
	          setValidations: this.setValidations,
	          setValue: this.setValue,
	          resetValue: this.resetValue,
	          getValue: this.getValue,
	          hasValue: this.hasValue,
	          getErrorMessage: this.getErrorMessage,
	          getErrorMessages: this.getErrorMessages,
	          isFormDisabled: this.isFormDisabled,
	          isValid: this.isValid,
	          isPristine: this.isPristine,
	          isFormSubmitted: this.isFormSubmitted,
	          isRequired: this.isRequired,
	          showRequired: this.showRequired,
	          showError: this.showError,
	          isValidValue: this.isValidValue
	        }, this.props));
	      }
	    });
	  };
	};
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ },

/***/ 383:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _getPrototypeOf = __webpack_require__(316);

	var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

	var _classCallCheck2 = __webpack_require__(321);

	var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

	var _createClass2 = __webpack_require__(322);

	var _createClass3 = _interopRequireDefault(_createClass2);

	var _possibleConstructorReturn2 = __webpack_require__(326);

	var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);

	var _inherits2 = __webpack_require__(360);

	var _inherits3 = _interopRequireDefault(_inherits2);

	var _react = __webpack_require__(24);

	var _react2 = _interopRequireDefault(_react);

	var _formsyReact = __webpack_require__(376);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var StandardInput = function (_Component) {
	  (0, _inherits3.default)(StandardInput, _Component);

	  function StandardInput(props) {
	    (0, _classCallCheck3.default)(this, StandardInput);

	    var _this = (0, _possibleConstructorReturn3.default)(this, (StandardInput.__proto__ || (0, _getPrototypeOf2.default)(StandardInput)).call(this, props));

	    _this.handleChange = _this.handleChange.bind(_this);
	    return _this;
	  }

	  (0, _createClass3.default)(StandardInput, [{
	    key: 'handleChange',
	    value: function handleChange(e) {
	      var handleValidationChange = this.props.handleValidationChange;


	      if (handleValidationChange) {
	        if (this.props.isValidValue(e.target.value)) handleValidationChange('VALID');else {
	          if (e.target.value !== '') handleValidationChange('INVALID');else handleValidationChange('INVALID_EMPTY');
	        }
	      }

	      this.props.setValue(e.target.value);
	    }
	  }, {
	    key: 'render',
	    value: function render() {
	      var _this2 = this;

	      var _props = this.props,
	          placeholder = _props.placeholder,
	          handleValidationChange = _props.handleValidationChange,
	          type = _props.type;

	      var inputType = type ? type : 'text';

	      var result = null;
	      if (handleValidationChange) result = _react2.default.createElement('input', { type: inputType,
	        className: 'form-control',
	        placeholder: placeholder,
	        value: this.props.getValue(),
	        onChange: function onChange(e) {
	          return _this2.handleChange(e);
	        }
	      });else result = _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement('input', { type: inputType,
	          className: 'form-control',
	          placeholder: placeholder,
	          value: this.props.getValue(),
	          onChange: function onChange(e) {
	            return _this2.props.setValue(e.target.value);
	          }
	        }),
	        _react2.default.createElement(
	          'span',
	          { className: 'validation-error' },
	          this.props.getErrorMessage()
	        )
	      );

	      return result;
	    }
	  }]);
	  return StandardInput;
	}(_react.Component);

	StandardInput.propTypes = {
	  placeholder: _react.PropTypes.string.isRequired,
	  type: _react.PropTypes.string,
	  handleValidationChange: _react.PropTypes.func
	};

	exports.default = (0, _formsyReact.HOC)(StandardInput);

/***/ },

/***/ 390:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ }

});