webpackJsonp([5],{

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

/***/ 393:
/***/ function(module, exports, __webpack_require__) {

	module.exports = { "default": __webpack_require__(394), __esModule: true };

/***/ },

/***/ 394:
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(357);
	__webpack_require__(330);
	__webpack_require__(341);
	__webpack_require__(395);
	__webpack_require__(409);
	module.exports = __webpack_require__(7).Set;

/***/ },

/***/ 395:
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	var strong = __webpack_require__(396);

	// 23.2 Set Objects
	module.exports = __webpack_require__(405)('Set', function(get){
	  return function Set(){ return get(this, arguments.length > 0 ? arguments[0] : undefined); };
	}, {
	  // 23.2.3.1 Set.prototype.add(value)
	  add: function add(value){
	    return strong.def(this, value = value === 0 ? 0 : value, value);
	  }
	}, strong);

/***/ },

/***/ 396:
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	var dP          = __webpack_require__(11).f
	  , create      = __webpack_require__(337)
	  , redefineAll = __webpack_require__(397)
	  , ctx         = __webpack_require__(8)
	  , anInstance  = __webpack_require__(398)
	  , defined     = __webpack_require__(285)
	  , forOf       = __webpack_require__(399)
	  , $iterDefine = __webpack_require__(332)
	  , step        = __webpack_require__(344)
	  , setSpecies  = __webpack_require__(404)
	  , DESCRIPTORS = __webpack_require__(15)
	  , fastKey     = __webpack_require__(349).fastKey
	  , SIZE        = DESCRIPTORS ? '_s' : 'size';

	var getEntry = function(that, key){
	  // fast case
	  var index = fastKey(key), entry;
	  if(index !== 'F')return that._i[index];
	  // frozen object case
	  for(entry = that._f; entry; entry = entry.n){
	    if(entry.k == key)return entry;
	  }
	};

	module.exports = {
	  getConstructor: function(wrapper, NAME, IS_MAP, ADDER){
	    var C = wrapper(function(that, iterable){
	      anInstance(that, C, NAME, '_i');
	      that._i = create(null); // index
	      that._f = undefined;    // first entry
	      that._l = undefined;    // last entry
	      that[SIZE] = 0;         // size
	      if(iterable != undefined)forOf(iterable, IS_MAP, that[ADDER], that);
	    });
	    redefineAll(C.prototype, {
	      // 23.1.3.1 Map.prototype.clear()
	      // 23.2.3.2 Set.prototype.clear()
	      clear: function clear(){
	        for(var that = this, data = that._i, entry = that._f; entry; entry = entry.n){
	          entry.r = true;
	          if(entry.p)entry.p = entry.p.n = undefined;
	          delete data[entry.i];
	        }
	        that._f = that._l = undefined;
	        that[SIZE] = 0;
	      },
	      // 23.1.3.3 Map.prototype.delete(key)
	      // 23.2.3.4 Set.prototype.delete(value)
	      'delete': function(key){
	        var that  = this
	          , entry = getEntry(that, key);
	        if(entry){
	          var next = entry.n
	            , prev = entry.p;
	          delete that._i[entry.i];
	          entry.r = true;
	          if(prev)prev.n = next;
	          if(next)next.p = prev;
	          if(that._f == entry)that._f = next;
	          if(that._l == entry)that._l = prev;
	          that[SIZE]--;
	        } return !!entry;
	      },
	      // 23.2.3.6 Set.prototype.forEach(callbackfn, thisArg = undefined)
	      // 23.1.3.5 Map.prototype.forEach(callbackfn, thisArg = undefined)
	      forEach: function forEach(callbackfn /*, that = undefined */){
	        anInstance(this, C, 'forEach');
	        var f = ctx(callbackfn, arguments.length > 1 ? arguments[1] : undefined, 3)
	          , entry;
	        while(entry = entry ? entry.n : this._f){
	          f(entry.v, entry.k, this);
	          // revert to the last existing entry
	          while(entry && entry.r)entry = entry.p;
	        }
	      },
	      // 23.1.3.7 Map.prototype.has(key)
	      // 23.2.3.7 Set.prototype.has(value)
	      has: function has(key){
	        return !!getEntry(this, key);
	      }
	    });
	    if(DESCRIPTORS)dP(C.prototype, 'size', {
	      get: function(){
	        return defined(this[SIZE]);
	      }
	    });
	    return C;
	  },
	  def: function(that, key, value){
	    var entry = getEntry(that, key)
	      , prev, index;
	    // change existing entry
	    if(entry){
	      entry.v = value;
	    // create new entry
	    } else {
	      that._l = entry = {
	        i: index = fastKey(key, true), // <- index
	        k: key,                        // <- key
	        v: value,                      // <- value
	        p: prev = that._l,             // <- previous entry
	        n: undefined,                  // <- next entry
	        r: false                       // <- removed
	      };
	      if(!that._f)that._f = entry;
	      if(prev)prev.n = entry;
	      that[SIZE]++;
	      // add to index
	      if(index !== 'F')that._i[index] = entry;
	    } return that;
	  },
	  getEntry: getEntry,
	  setStrong: function(C, NAME, IS_MAP){
	    // add .keys, .values, .entries, [@@iterator]
	    // 23.1.3.4, 23.1.3.8, 23.1.3.11, 23.1.3.12, 23.2.3.5, 23.2.3.8, 23.2.3.10, 23.2.3.11
	    $iterDefine(C, NAME, function(iterated, kind){
	      this._t = iterated;  // target
	      this._k = kind;      // kind
	      this._l = undefined; // previous
	    }, function(){
	      var that  = this
	        , kind  = that._k
	        , entry = that._l;
	      // revert to the last existing entry
	      while(entry && entry.r)entry = entry.p;
	      // get next entry
	      if(!that._t || !(that._l = entry = entry ? entry.n : that._t._f)){
	        // or finish the iteration
	        that._t = undefined;
	        return step(1);
	      }
	      // return step by kind
	      if(kind == 'keys'  )return step(0, entry.k);
	      if(kind == 'values')return step(0, entry.v);
	      return step(0, [entry.k, entry.v]);
	    }, IS_MAP ? 'entries' : 'values' , !IS_MAP, true);

	    // add [@@species], 23.1.2.2, 23.2.2.2
	    setSpecies(NAME);
	  }
	};

/***/ },

/***/ 397:
/***/ function(module, exports, __webpack_require__) {

	var hide = __webpack_require__(10);
	module.exports = function(target, src, safe){
	  for(var key in src){
	    if(safe && target[key])target[key] = src[key];
	    else hide(target, key, src[key]);
	  } return target;
	};

/***/ },

/***/ 398:
/***/ function(module, exports) {

	module.exports = function(it, Constructor, name, forbiddenField){
	  if(!(it instanceof Constructor) || (forbiddenField !== undefined && forbiddenField in it)){
	    throw TypeError(name + ': incorrect invocation!');
	  } return it;
	};

/***/ },

/***/ 399:
/***/ function(module, exports, __webpack_require__) {

	var ctx         = __webpack_require__(8)
	  , call        = __webpack_require__(400)
	  , isArrayIter = __webpack_require__(401)
	  , anObject    = __webpack_require__(12)
	  , toLength    = __webpack_require__(287)
	  , getIterFn   = __webpack_require__(402)
	  , BREAK       = {}
	  , RETURN      = {};
	var exports = module.exports = function(iterable, entries, fn, that, ITERATOR){
	  var iterFn = ITERATOR ? function(){ return iterable; } : getIterFn(iterable)
	    , f      = ctx(fn, that, entries ? 2 : 1)
	    , index  = 0
	    , length, step, iterator, result;
	  if(typeof iterFn != 'function')throw TypeError(iterable + ' is not iterable!');
	  // fast case for arrays with default iterator
	  if(isArrayIter(iterFn))for(length = toLength(iterable.length); length > index; index++){
	    result = entries ? f(anObject(step = iterable[index])[0], step[1]) : f(iterable[index]);
	    if(result === BREAK || result === RETURN)return result;
	  } else for(iterator = iterFn.call(iterable); !(step = iterator.next()).done; ){
	    result = call(iterator, f, step.value, entries);
	    if(result === BREAK || result === RETURN)return result;
	  }
	};
	exports.BREAK  = BREAK;
	exports.RETURN = RETURN;

/***/ },

/***/ 400:
/***/ function(module, exports, __webpack_require__) {

	// call something on iterator step with safe closing on error
	var anObject = __webpack_require__(12);
	module.exports = function(iterator, fn, value, entries){
	  try {
	    return entries ? fn(anObject(value)[0], value[1]) : fn(value);
	  // 7.4.6 IteratorClose(iterator, completion)
	  } catch(e){
	    var ret = iterator['return'];
	    if(ret !== undefined)anObject(ret.call(iterator));
	    throw e;
	  }
	};

/***/ },

/***/ 401:
/***/ function(module, exports, __webpack_require__) {

	// check on default Array iterator
	var Iterators  = __webpack_require__(335)
	  , ITERATOR   = __webpack_require__(340)('iterator')
	  , ArrayProto = Array.prototype;

	module.exports = function(it){
	  return it !== undefined && (Iterators.Array === it || ArrayProto[ITERATOR] === it);
	};

/***/ },

/***/ 402:
/***/ function(module, exports, __webpack_require__) {

	var classof   = __webpack_require__(403)
	  , ITERATOR  = __webpack_require__(340)('iterator')
	  , Iterators = __webpack_require__(335);
	module.exports = __webpack_require__(7).getIteratorMethod = function(it){
	  if(it != undefined)return it[ITERATOR]
	    || it['@@iterator']
	    || Iterators[classof(it)];
	};

/***/ },

/***/ 403:
/***/ function(module, exports, __webpack_require__) {

	// getting tag from 19.1.3.6 Object.prototype.toString()
	var cof = __webpack_require__(23)
	  , TAG = __webpack_require__(340)('toStringTag')
	  // ES3 wrong here
	  , ARG = cof(function(){ return arguments; }()) == 'Arguments';

	// fallback for IE11 Script Access Denied error
	var tryGet = function(it, key){
	  try {
	    return it[key];
	  } catch(e){ /* empty */ }
	};

	module.exports = function(it){
	  var O, T, B;
	  return it === undefined ? 'Undefined' : it === null ? 'Null'
	    // @@toStringTag case
	    : typeof (T = tryGet(O = Object(it), TAG)) == 'string' ? T
	    // builtinTag case
	    : ARG ? cof(O)
	    // ES3 arguments fallback
	    : (B = cof(O)) == 'Object' && typeof O.callee == 'function' ? 'Arguments' : B;
	};

/***/ },

/***/ 404:
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	var global      = __webpack_require__(6)
	  , core        = __webpack_require__(7)
	  , dP          = __webpack_require__(11)
	  , DESCRIPTORS = __webpack_require__(15)
	  , SPECIES     = __webpack_require__(340)('species');

	module.exports = function(KEY){
	  var C = typeof core[KEY] == 'function' ? core[KEY] : global[KEY];
	  if(DESCRIPTORS && C && !C[SPECIES])dP.f(C, SPECIES, {
	    configurable: true,
	    get: function(){ return this; }
	  });
	};

/***/ },

/***/ 405:
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	var global         = __webpack_require__(6)
	  , $export        = __webpack_require__(5)
	  , meta           = __webpack_require__(349)
	  , fails          = __webpack_require__(16)
	  , hide           = __webpack_require__(10)
	  , redefineAll    = __webpack_require__(397)
	  , forOf          = __webpack_require__(399)
	  , anInstance     = __webpack_require__(398)
	  , isObject       = __webpack_require__(13)
	  , setToStringTag = __webpack_require__(339)
	  , dP             = __webpack_require__(11).f
	  , each           = __webpack_require__(406)(0)
	  , DESCRIPTORS    = __webpack_require__(15);

	module.exports = function(NAME, wrapper, methods, common, IS_MAP, IS_WEAK){
	  var Base  = global[NAME]
	    , C     = Base
	    , ADDER = IS_MAP ? 'set' : 'add'
	    , proto = C && C.prototype
	    , O     = {};
	  if(!DESCRIPTORS || typeof C != 'function' || !(IS_WEAK || proto.forEach && !fails(function(){
	    new C().entries().next();
	  }))){
	    // create collection constructor
	    C = common.getConstructor(wrapper, NAME, IS_MAP, ADDER);
	    redefineAll(C.prototype, methods);
	    meta.NEED = true;
	  } else {
	    C = wrapper(function(target, iterable){
	      anInstance(target, C, NAME, '_c');
	      target._c = new Base;
	      if(iterable != undefined)forOf(iterable, IS_MAP, target[ADDER], target);
	    });
	    each('add,clear,delete,forEach,get,has,set,keys,values,entries,toJSON'.split(','),function(KEY){
	      var IS_ADDER = KEY == 'add' || KEY == 'set';
	      if(KEY in proto && !(IS_WEAK && KEY == 'clear'))hide(C.prototype, KEY, function(a, b){
	        anInstance(this, C, KEY);
	        if(!IS_ADDER && IS_WEAK && !isObject(a))return KEY == 'get' ? undefined : false;
	        var result = this._c[KEY](a === 0 ? 0 : a, b);
	        return IS_ADDER ? this : result;
	      });
	    });
	    if('size' in proto)dP(C.prototype, 'size', {
	      get: function(){
	        return this._c.size;
	      }
	    });
	  }

	  setToStringTag(C, NAME);

	  O[NAME] = C;
	  $export($export.G + $export.W + $export.F, O);

	  if(!IS_WEAK)common.setStrong(C, NAME, IS_MAP);

	  return C;
	};

/***/ },

/***/ 406:
/***/ function(module, exports, __webpack_require__) {

	// 0 -> Array#forEach
	// 1 -> Array#map
	// 2 -> Array#filter
	// 3 -> Array#some
	// 4 -> Array#every
	// 5 -> Array#find
	// 6 -> Array#findIndex
	var ctx      = __webpack_require__(8)
	  , IObject  = __webpack_require__(284)
	  , toObject = __webpack_require__(296)
	  , toLength = __webpack_require__(287)
	  , asc      = __webpack_require__(407);
	module.exports = function(TYPE, $create){
	  var IS_MAP        = TYPE == 1
	    , IS_FILTER     = TYPE == 2
	    , IS_SOME       = TYPE == 3
	    , IS_EVERY      = TYPE == 4
	    , IS_FIND_INDEX = TYPE == 6
	    , NO_HOLES      = TYPE == 5 || IS_FIND_INDEX
	    , create        = $create || asc;
	  return function($this, callbackfn, that){
	    var O      = toObject($this)
	      , self   = IObject(O)
	      , f      = ctx(callbackfn, that, 3)
	      , length = toLength(self.length)
	      , index  = 0
	      , result = IS_MAP ? create($this, length) : IS_FILTER ? create($this, 0) : undefined
	      , val, res;
	    for(;length > index; index++)if(NO_HOLES || index in self){
	      val = self[index];
	      res = f(val, index, O);
	      if(TYPE){
	        if(IS_MAP)result[index] = res;            // map
	        else if(res)switch(TYPE){
	          case 3: return true;                    // some
	          case 5: return val;                     // find
	          case 6: return index;                   // findIndex
	          case 2: result.push(val);               // filter
	        } else if(IS_EVERY)return false;          // every
	      }
	    }
	    return IS_FIND_INDEX ? -1 : IS_SOME || IS_EVERY ? IS_EVERY : result;
	  };
	};

/***/ },

/***/ 407:
/***/ function(module, exports, __webpack_require__) {

	// 9.4.2.3 ArraySpeciesCreate(originalArray, length)
	var speciesConstructor = __webpack_require__(408);

	module.exports = function(original, length){
	  return new (speciesConstructor(original))(length);
	};

/***/ },

/***/ 408:
/***/ function(module, exports, __webpack_require__) {

	var isObject = __webpack_require__(13)
	  , isArray  = __webpack_require__(353)
	  , SPECIES  = __webpack_require__(340)('species');

	module.exports = function(original){
	  var C;
	  if(isArray(original)){
	    C = original.constructor;
	    // cross-realm fallback
	    if(typeof C == 'function' && (C === Array || isArray(C.prototype)))C = undefined;
	    if(isObject(C)){
	      C = C[SPECIES];
	      if(C === null)C = undefined;
	    }
	  } return C === undefined ? Array : C;
	};

/***/ },

/***/ 409:
/***/ function(module, exports, __webpack_require__) {

	// https://github.com/DavidBruant/Map-Set.prototype.toJSON
	var $export  = __webpack_require__(5);

	$export($export.P + $export.R, 'Set', {toJSON: __webpack_require__(410)('Set')});

/***/ },

/***/ 410:
/***/ function(module, exports, __webpack_require__) {

	// https://github.com/DavidBruant/Map-Set.prototype.toJSON
	var classof = __webpack_require__(403)
	  , from    = __webpack_require__(411);
	module.exports = function(NAME){
	  return function toJSON(){
	    if(classof(this) != NAME)throw TypeError(NAME + "#toJSON isn't generic");
	    return from(this);
	  };
	};

/***/ },

/***/ 411:
/***/ function(module, exports, __webpack_require__) {

	var forOf = __webpack_require__(399);

	module.exports = function(iter, ITERATOR){
	  var result = [];
	  forOf(iter, false, result.push, result, ITERATOR);
	  return result;
	};


/***/ },

/***/ 412:
/***/ function(module, exports, __webpack_require__) {

	module.exports = { "default": __webpack_require__(413), __esModule: true };

/***/ },

/***/ 413:
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(414);
	module.exports = __webpack_require__(7).Object.keys;

/***/ },

/***/ 414:
/***/ function(module, exports, __webpack_require__) {

	// 19.1.2.14 Object.keys(O)
	var toObject = __webpack_require__(296)
	  , $keys    = __webpack_require__(280);

	__webpack_require__(320)('keys', function(){
	  return function keys(it){
	    return $keys(toObject(it));
	  };
	});

/***/ },

/***/ 435:
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

	var _StandardInput = __webpack_require__(383);

	var _StandardInput2 = _interopRequireDefault(_StandardInput);

	var _ComboBoxInput = __webpack_require__(436);

	var _ComboBoxInput2 = _interopRequireDefault(_ComboBoxInput);

	var _jquery = __webpack_require__(389);

	var _jquery2 = _interopRequireDefault(_jquery);

	__webpack_require__(437);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var GuestsPopup = function (_Component) {
	  (0, _inherits3.default)(GuestsPopup, _Component);

	  function GuestsPopup(props) {
	    (0, _classCallCheck3.default)(this, GuestsPopup);

	    var _this = (0, _possibleConstructorReturn3.default)(this, (GuestsPopup.__proto__ || (0, _getPrototypeOf2.default)(GuestsPopup)).call(this, props));

	    _this.nationalities = [{
	      name: 'BE',
	      value: 'BE'
	    }, {
	      name: 'DE',
	      value: 'DE'
	    }, {
	      name: 'FR',
	      value: 'FR'
	    }, {
	      name: 'GB',
	      value: 'GB'
	    }, {
	      name: 'LU',
	      value: 'LU'
	    }, {
	      name: 'NL',
	      value: 'NL'
	    }];
	    return _this;
	  }

	  (0, _createClass3.default)(GuestsPopup, [{
	    key: 'render',
	    value: function render() {
	      var _props = this.props,
	          modalData = _props.modalData,
	          model = _props.model,
	          handleOnValid = _props.handleOnValid,
	          handleOnInvalid = _props.handleOnInvalid,
	          handleSaveGuest = _props.handleSaveGuest,
	          handleCancelGuest = _props.handleCancelGuest;


	      return _react2.default.createElement(
	        'div',
	        { id: modalData.id, className: 'guests-modal modal fade', tabIndex: '-1', role: 'dialog' },
	        _react2.default.createElement(
	          'div',
	          { className: 'modal-dialog', role: 'document' },
	          _react2.default.createElement(
	            'div',
	            { className: 'modal-content' },
	            _react2.default.createElement(
	              'div',
	              { className: 'modal-header' },
	              _react2.default.createElement(
	                'h4',
	                { className: 'modal-title' },
	                modalData.title
	              )
	            ),
	            _react2.default.createElement(
	              'div',
	              { className: 'modal-body' },
	              _react2.default.createElement(
	                _formsyReact.Form,
	                { onSubmit: handleSaveGuest,
	                  onValid: handleOnValid,
	                  onInvalid: handleOnInvalid,
	                  className: 'form-group guests-modal',
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
	                    { htmlFor: 'lastName' },
	                    'Last name *'
	                  ),
	                  _react2.default.createElement(_StandardInput2.default, { value: model.lastName,
	                    name: 'lastName',
	                    title: 'Last name',
	                    placeholder: 'min. 3 letters',
	                    validations: 'isSpecialWords,minLength:3',
	                    validationError: 'This is not a valid last name',
	                    required: true
	                  })
	                ),
	                _react2.default.createElement(
	                  'div',
	                  { className: 'form-group' },
	                  _react2.default.createElement(
	                    'label',
	                    { htmlFor: 'firstName' },
	                    'First name *'
	                  ),
	                  _react2.default.createElement(_StandardInput2.default, { value: model.firstName,
	                    name: 'firstName',
	                    title: 'First name',
	                    placeholder: 'min. 3 letters',
	                    validations: 'isSpecialWords,minLength:3',
	                    validationError: 'This is not a valid first name',
	                    required: true
	                  })
	                ),
	                _react2.default.createElement(
	                  'div',
	                  { className: 'form-group' },
	                  _react2.default.createElement(
	                    'label',
	                    { htmlFor: 'nationality' },
	                    'Nationality *'
	                  ),
	                  _react2.default.createElement(_ComboBoxInput2.default, { options: this.nationalities, value: model.nationality, name: 'nationality', title: 'Nationality' })
	                ),
	                _react2.default.createElement(
	                  'div',
	                  { className: 'form-group' },
	                  _react2.default.createElement(
	                    'label',
	                    { htmlFor: 'identification' },
	                    'ID'
	                  ),
	                  _react2.default.createElement(_StandardInput2.default, { value: model.identification,
	                    name: 'identification',
	                    title: 'ID',
	                    placeholder: 'min. 5 letters/numbers',
	                    validations: 'isAlphanumeric,minLength:5',
	                    validationError: 'This is not a valid ID'
	                  })
	                ),
	                _react2.default.createElement(
	                  'div',
	                  { className: 'form-group' },
	                  _react2.default.createElement(
	                    'label',
	                    { htmlFor: 'email' },
	                    'Email'
	                  ),
	                  _react2.default.createElement(_StandardInput2.default, { value: model.email,
	                    name: 'email',
	                    title: 'Email',
	                    placeholder: '',
	                    validations: 'isEmail',
	                    validationError: 'This is not a valid email address'
	                  })
	                ),
	                _react2.default.createElement(
	                  'button',
	                  { id: 'submitForm', hidden: true, type: 'submit' },
	                  'Submit'
	                )
	              )
	            ),
	            _react2.default.createElement(
	              'div',
	              { className: 'modal-footer' },
	              _react2.default.createElement(
	                'button',
	                { type: 'button',
	                  className: 'btn btn-default',
	                  'data-dismiss': 'modal',
	                  onClick: function onClick() {
	                    if (handleCancelGuest) handleCancelGuest();
	                  } },
	                'Cancel'
	              ),
	              _react2.default.createElement(
	                'button',
	                { type: 'button',
	                  className: "btn btn-primary " + (modalData.canSubmit ? "" : "disabled"),
	                  onClick: function onClick() {
	                    if (modalData.canSubmit) (0, _jquery2.default)('#submitForm').click();
	                  } },
	                'Save'
	              )
	            )
	          )
	        )
	      );
	    }
	  }]);
	  return GuestsPopup;
	}(_react.Component);

	exports.default = GuestsPopup;


	GuestsPopup.propTypes = {
	  modalData: _react.PropTypes.object.isRequired,
	  model: _react.PropTypes.object.isRequired,
	  handleOnValid: _react.PropTypes.func.isRequired,
	  handleOnInvalid: _react.PropTypes.func.isRequired,
	  handleSaveGuest: _react.PropTypes.func.isRequired,
	  handleCancelGuest: _react.PropTypes.func
	};

/***/ },

/***/ 436:
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

	var ComboBoxInput = function (_Component) {
	  (0, _inherits3.default)(ComboBoxInput, _Component);

	  function ComboBoxInput() {
	    (0, _classCallCheck3.default)(this, ComboBoxInput);
	    return (0, _possibleConstructorReturn3.default)(this, (ComboBoxInput.__proto__ || (0, _getPrototypeOf2.default)(ComboBoxInput)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(ComboBoxInput, [{
	    key: 'render',
	    value: function render() {
	      var _this2 = this;

	      var options = this.props.options;


	      var optionComponents = options.map(function (option, index) {
	        return _react2.default.createElement(
	          'option',
	          { key: index, value: option.value },
	          option.name
	        );
	      });

	      return _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement(
	          'select',
	          { className: 'form-control',
	            name: this.props.name,
	            value: this.props.getValue(),
	            onChange: function onChange(e) {
	              return _this2.props.setValue(e.target.value);
	            }
	          },
	          optionComponents
	        ),
	        _react2.default.createElement(
	          'span',
	          { className: 'validation-error' },
	          this.props.getErrorMessage()
	        )
	      );
	    }
	  }]);
	  return ComboBoxInput;
	}(_react.Component);

	;

	ComboBoxInput.propTypes = {
	  options: _react.PropTypes.array.isRequired
	};

	exports.default = (0, _formsyReact.HOC)(ComboBoxInput);

/***/ },

/***/ 437:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 551:
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

	__webpack_require__(552);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var SectionHeader = function (_Component) {
	  (0, _inherits3.default)(SectionHeader, _Component);

	  function SectionHeader() {
	    (0, _classCallCheck3.default)(this, SectionHeader);
	    return (0, _possibleConstructorReturn3.default)(this, (SectionHeader.__proto__ || (0, _getPrototypeOf2.default)(SectionHeader)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(SectionHeader, [{
	    key: 'render',
	    value: function render() {
	      return _react2.default.createElement(
	        'div',
	        { className: 'section-header' },
	        this.props.children
	      );
	    }
	  }]);
	  return SectionHeader;
	}(_react.Component);

	SectionHeader.propTypes = {
	  children: _react.PropTypes.object.isRequired
	};

	exports.default = SectionHeader;

/***/ },

/***/ 552:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 553:
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

	__webpack_require__(554);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var SectionContent = function (_Component) {
	  (0, _inherits3.default)(SectionContent, _Component);

	  function SectionContent() {
	    (0, _classCallCheck3.default)(this, SectionContent);
	    return (0, _possibleConstructorReturn3.default)(this, (SectionContent.__proto__ || (0, _getPrototypeOf2.default)(SectionContent)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(SectionContent, [{
	    key: 'render',
	    value: function render() {
	      var overlayEnabled = this.props.overlayEnabled;


	      return _react2.default.createElement(
	        'div',
	        { className: 'section-content' },
	        this.props.children,
	        overlayEnabled && _react2.default.createElement(
	          'div',
	          { className: 'overlay' },
	          _react2.default.createElement(
	            'div',
	            { className: 'sk-circle' },
	            _react2.default.createElement('div', { className: 'sk-circle1 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle2 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle3 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle4 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle5 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle6 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle7 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle8 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle9 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle10 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle11 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle12 sk-child' })
	          )
	        )
	      );
	    }
	  }]);
	  return SectionContent;
	}(_react.Component);

	SectionContent.propTypes = {
	  overlayEnabled: _react.PropTypes.bool.isRequired,
	  children: _react.PropTypes.object.isRequired
	};

	exports.default = SectionContent;

/***/ },

/***/ 554:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 613:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _set = __webpack_require__(393);

	var _set2 = _interopRequireDefault(_set);

	var _extends2 = __webpack_require__(275);

	var _extends3 = _interopRequireDefault(_extends2);

	var _keys = __webpack_require__(412);

	var _keys2 = _interopRequireDefault(_keys);

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

	var _Guests = __webpack_require__(614);

	var _Guests2 = _interopRequireDefault(_Guests);

	var _actions = __webpack_require__(557);

	var types = _interopRequireWildcard(_actions);

	var _guestActions = __webpack_require__(558);

	var guestTypes = _interopRequireWildcard(_guestActions);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var GuestsContainer = function (_Component) {
	  (0, _inherits3.default)(GuestsContainer, _Component);

	  function GuestsContainer(props) {
	    (0, _classCallCheck3.default)(this, GuestsContainer);

	    var _this = (0, _possibleConstructorReturn3.default)(this, (GuestsContainer.__proto__ || (0, _getPrototypeOf2.default)(GuestsContainer)).call(this, props));

	    _this.handleAddGuest = _this.handleAddGuest.bind(_this);
	    _this.handleUpdateGuest = _this.handleUpdateGuest.bind(_this);
	    _this.handleDeleteGuest = _this.handleDeleteGuest.bind(_this);
	    _this.addInfoAboutDeletion = _this.addInfoAboutDeletion.bind(_this);
	    return _this;
	  }

	  (0, _createClass3.default)(GuestsContainer, [{
	    key: 'componentDidMount',
	    value: function componentDidMount() {
	      this.props.dispatch(guestTypes.fetchGuests());
	    }
	  }, {
	    key: 'handleAddGuest',
	    value: function handleAddGuest(newUser) {
	      this.props.dispatch(guestTypes.addGuest(newUser));
	      this.props.dispatch(guestTypes.fetchGuests());
	    }
	  }, {
	    key: 'handleUpdateGuest',
	    value: function handleUpdateGuest(updatedUser) {
	      this.props.dispatch(guestTypes.updateGuest(updatedUser));
	      this.props.dispatch(guestTypes.fetchGuests());
	    }
	  }, {
	    key: 'handleDeleteGuest',
	    value: function handleDeleteGuest(id) {
	      this.props.dispatch(guestTypes.deleteGuest(id));
	      this.props.dispatch(guestTypes.fetchGuests());
	    }
	  }, {
	    key: 'addInfoAboutDeletion',
	    value: function addInfoAboutDeletion(guestItems, stayEntries) {
	      return guestItems.map(function (guest) {
	        var isDeletionAllowed = true;
	        (0, _keys2.default)(stayEntries).forEach(function (key) {
	          var stay = stayEntries[key];
	          if (stay.guest.id === guest.id) isDeletionAllowed = false;
	        });
	        return (0, _extends3.default)({}, guest, { isDeletionAllowed: isDeletionAllowed });
	      });
	    }
	  }, {
	    key: 'render',
	    value: function render() {
	      var _props = this.props,
	          guests = _props.guests,
	          generic = _props.generic,
	          stays = _props.stays;


	      var guestsSorted = guests.items.sort(byNameComparator);

	      var pendingStatuses = new _set2.default([types.UPDATE_GENERIC_PENDING, types.POST_GENERIC_PENDING, types.DELETE_GENERIC_PENDING]);
	      var isPendingAction = pendingStatuses.has(generic.status);

	      var guestsWithDeletionInfo = this.addInfoAboutDeletion(guestsSorted, stays.entries);

	      return _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement(_Guests2.default, { guests: guestsWithDeletionInfo,
	          handleAddGuest: this.handleAddGuest,
	          handleUpdateGuest: this.handleUpdateGuest,
	          handleDeleteGuest: this.handleDeleteGuest,
	          isPendingAction: isPendingAction
	        })
	      );
	    }
	  }]);
	  return GuestsContainer;
	}(_react.Component);

	var mapStateToProps = function mapStateToProps(state) {
	  return {
	    guests: state.guests,
	    generic: state.generic,
	    stays: state.stays
	  };
	};

	GuestsContainer = (0, _reactRedux.connect)(mapStateToProps, null)(GuestsContainer);
	exports.default = GuestsContainer;

	//TODO needs to be changed

	var byNameComparator = function byNameComparator(guestA, guestB) {
	  if (guestA.lastName < guestB.lastName) return -1;else if (guestA.lastName > guestB.lastName) return 1;else {
	    if (guestA.firstName < guestB.firstName) return -1;else if (guestA.firstName > guestB.firstName) return 1;else {
	      if (guestA.identification < guestB.identification) return -1;else if (guestA.identification > guestB.identification) return 1;else return 0;
	    }
	  }
	};

/***/ },

/***/ 614:
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

	var _SectionHeader = __webpack_require__(551);

	var _SectionHeader2 = _interopRequireDefault(_SectionHeader);

	var _SectionContent = __webpack_require__(553);

	var _SectionContent2 = _interopRequireDefault(_SectionContent);

	var _GuestsEntry = __webpack_require__(615);

	var _GuestsEntry2 = _interopRequireDefault(_GuestsEntry);

	var _GuestsModal = __webpack_require__(435);

	var _GuestsModal2 = _interopRequireDefault(_GuestsModal);

	__webpack_require__(616);

	var _jquery = __webpack_require__(389);

	var _jquery2 = _interopRequireDefault(_jquery);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var Guests = function (_Component) {
	  (0, _inherits3.default)(Guests, _Component);

	  function Guests(props) {
	    (0, _classCallCheck3.default)(this, Guests);

	    var _this = (0, _possibleConstructorReturn3.default)(this, (Guests.__proto__ || (0, _getPrototypeOf2.default)(Guests)).call(this, props));

	    _this.handleAddGuest = _this.handleAddGuest.bind(_this);
	    _this.handleEditGuest = _this.handleEditGuest.bind(_this);
	    _this.handleDeleteGuest = _this.handleDeleteGuest.bind(_this);
	    _this.handleSaveGuest = _this.handleSaveGuest.bind(_this);
	    _this.handleModelValid = _this.handleModelValid.bind(_this);
	    _this.handleModelInvalid = _this.handleModelInvalid.bind(_this);

	    _this.emptyModel = {
	      id: -1,
	      firstName: '',
	      lastName: '',
	      nationality: 'BE',
	      identification: '',
	      email: ''
	    };
	    _this.state = {
	      modal: {
	        modalData: {
	          id: 'guestsModal',
	          title: 'Adding a new guest',
	          canSubmit: false
	        },
	        model: (0, _extends3.default)({}, _this.emptyModel)
	      }
	    };
	    return _this;
	  }

	  (0, _createClass3.default)(Guests, [{
	    key: 'handleAddGuest',
	    value: function handleAddGuest() {
	      var modal = (0, _extends3.default)({}, this.state.modal, { model: (0, _extends3.default)({}, this.emptyModel) });
	      this.setState((0, _extends3.default)({}, this.state, { modal: modal }));
	      (0, _jquery2.default)('#' + this.state.modal.modalData.id).modal('show');
	    }
	  }, {
	    key: 'handleEditGuest',
	    value: function handleEditGuest(id) {
	      var model = null;
	      this.props.guests.forEach(function (guest) {
	        if (guest.id === id) {
	          model = {
	            id: id,
	            firstName: guest.firstName,
	            lastName: guest.lastName,
	            nationality: guest.nationality,
	            identification: guest.identification,
	            email: guest.email
	          };
	        }
	      });

	      var modal = (0, _extends3.default)({}, this.state.modal, { model: model });
	      this.setState((0, _extends3.default)({}, this.state, { modal: modal }));
	      (0, _jquery2.default)('#' + this.state.modal.modalData.id).modal('show');
	    }
	  }, {
	    key: 'handleDeleteGuest',
	    value: function handleDeleteGuest(id) {
	      this.props.handleDeleteGuest(id);
	    }
	  }, {
	    key: 'handleSaveGuest',
	    value: function handleSaveGuest(model) {
	      var currentId = this.state.modal.model.id;
	      if (currentId === -1) this.props.handleAddGuest(model);else this.props.handleUpdateGuest((0, _extends3.default)({}, model, { id: currentId }));

	      (0, _jquery2.default)('#' + this.state.modal.modalData.id).modal('hide');
	    }
	  }, {
	    key: 'handleModelValid',
	    value: function handleModelValid() {
	      var modalData = (0, _extends3.default)({}, this.state.modal.modalData, { canSubmit: true });
	      var modal = (0, _extends3.default)({}, this.state.modal, { modalData: modalData });
	      this.setState((0, _extends3.default)({}, this.state, { modal: modal }));
	    }
	  }, {
	    key: 'handleModelInvalid',
	    value: function handleModelInvalid() {
	      var modalData = (0, _extends3.default)({}, this.state.modal.modalData, { canSubmit: false });
	      var modal = (0, _extends3.default)({}, this.state.modal, { modalData: modalData });
	      this.setState((0, _extends3.default)({}, this.state, { modal: modal }));
	    }
	  }, {
	    key: 'render',
	    value: function render() {
	      var _this2 = this;

	      var _props = this.props,
	          guests = _props.guests,
	          isPendingAction = _props.isPendingAction;


	      var entries = guests.map(function (guest, index) {
	        return _react2.default.createElement(_GuestsEntry2.default, { key: index + 1,
	          nr: index + 1,
	          guest: guest,
	          handleEditGuest: _this2.handleEditGuest,
	          handleDeleteGuest: _this2.handleDeleteGuest
	        });
	      });

	      return _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement(
	          _SectionHeader2.default,
	          null,
	          _react2.default.createElement(
	            'span',
	            null,
	            'Guests'
	          )
	        ),
	        _react2.default.createElement(
	          _SectionContent2.default,
	          { overlayEnabled: isPendingAction },
	          _react2.default.createElement(
	            'div',
	            { className: 'row' },
	            _react2.default.createElement(
	              'div',
	              { className: 'guests col-xs-offset-2 col-xs-8' },
	              _react2.default.createElement(
	                'button',
	                { onClick: function onClick(event) {
	                    event.preventDefault();_this2.handleAddGuest();
	                  },
	                  type: 'button', className: 'btn btn-primary pull-right', 'data-toggle': 'modal' },
	                'Add'
	              ),
	              _react2.default.createElement(
	                'table',
	                { className: 'table table-hover' },
	                _react2.default.createElement(
	                  'thead',
	                  null,
	                  _react2.default.createElement(
	                    'tr',
	                    null,
	                    _react2.default.createElement(
	                      'th',
	                      null,
	                      '#'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      null,
	                      'Last name'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      null,
	                      'First name'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      null,
	                      'Nationality'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      null,
	                      'ID'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      null,
	                      'Email'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      { className: 'col-xs-2' },
	                      'Actions'
	                    )
	                  )
	                ),
	                _react2.default.createElement(
	                  'tbody',
	                  null,
	                  entries
	                )
	              )
	            )
	          )
	        ),
	        _react2.default.createElement(_GuestsModal2.default, { modalData: this.state.modal.modalData,
	          model: this.state.modal.model,
	          handleOnValid: this.handleModelValid,
	          handleOnInvalid: this.handleModelInvalid,
	          handleSaveGuest: this.handleSaveGuest
	        })
	      );
	    }
	  }]);
	  return Guests;
	}(_react.Component);

	exports.default = Guests;


	Guests.propTypes = {
	  guests: _react.PropTypes.array.isRequired,
	  isPendingAction: _react.PropTypes.bool.isRequired,
	  handleAddGuest: _react.PropTypes.func.isRequired,
	  handleUpdateGuest: _react.PropTypes.func.isRequired,
	  handleDeleteGuest: _react.PropTypes.func.isRequired
	};

/***/ },

/***/ 615:
/***/ function(module, exports, __webpack_require__) {

	"use strict";

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

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var GuestsEntry = function (_Component) {
	  (0, _inherits3.default)(GuestsEntry, _Component);

	  function GuestsEntry() {
	    (0, _classCallCheck3.default)(this, GuestsEntry);
	    return (0, _possibleConstructorReturn3.default)(this, (GuestsEntry.__proto__ || (0, _getPrototypeOf2.default)(GuestsEntry)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(GuestsEntry, [{
	    key: "render",
	    value: function render() {
	      var _props = this.props,
	          nr = _props.nr,
	          guest = _props.guest,
	          handleEditGuest = _props.handleEditGuest,
	          handleDeleteGuest = _props.handleDeleteGuest;


	      return _react2.default.createElement(
	        "tr",
	        { id: guest.id },
	        _react2.default.createElement(
	          "td",
	          null,
	          nr
	        ),
	        _react2.default.createElement(
	          "td",
	          null,
	          guest.lastName
	        ),
	        _react2.default.createElement(
	          "td",
	          null,
	          guest.firstName
	        ),
	        _react2.default.createElement(
	          "td",
	          null,
	          guest.nationality
	        ),
	        _react2.default.createElement(
	          "td",
	          null,
	          guest.identification
	        ),
	        _react2.default.createElement(
	          "td",
	          null,
	          guest.email
	        ),
	        _react2.default.createElement(
	          "td",
	          { className: "col-xs-2" },
	          _react2.default.createElement(
	            "div",
	            { className: "col-xs-6 active" },
	            _react2.default.createElement(
	              "a",
	              { href: "#", onClick: function onClick(e) {
	                  e.preventDefault();handleEditGuest(guest.id);
	                } },
	              _react2.default.createElement("i", { className: "fa fa-pencil fa-lg", "aria-hidden": "true" }),
	              _react2.default.createElement(
	                "div",
	                null,
	                "edit"
	              )
	            )
	          ),
	          _react2.default.createElement(
	            "div",
	            { className: "col-xs-6" + (guest.isDeletionAllowed ? " active" : "") },
	            _react2.default.createElement(
	              "a",
	              { href: "#", onClick: function onClick(e) {
	                  e.preventDefault();
	                  if (guest.isDeletionAllowed) handleDeleteGuest(guest.id);
	                } },
	              _react2.default.createElement("i", { className: "fa fa-trash-o fa-lg", "aria-hidden": "true" }),
	              _react2.default.createElement(
	                "div",
	                null,
	                "delete"
	              )
	            )
	          )
	        )
	      );
	    }
	  }]);
	  return GuestsEntry;
	}(_react.Component);

	exports.default = GuestsEntry;


	GuestsEntry.propTypes = {
	  nr: _react.PropTypes.number,
	  guest: _react.PropTypes.object.isRequired,
	  handleEditGuest: _react.PropTypes.func.isRequired,
	  handleDeleteGuest: _react.PropTypes.func.isRequired
	};

/***/ },

/***/ 616:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ }

});