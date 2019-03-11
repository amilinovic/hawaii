import en from 'date-fns/locale/en-GB';
import { Formik } from 'formik';
import moment from 'moment';
import React, { Component } from 'react';
import DatePicker, { registerLocale } from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker-cssmodules.css';
import 'react-datepicker/dist/react-datepicker.css';
import { connect } from 'react-redux';
import * as Yup from 'yup';

registerLocale('en-GB', en);

const validationSchema = Yup.object().shape({
  reason: Yup.string().required(),
  absence: Yup.object({
    id: Yup.string().required()
  })
});

class RequestForm extends Component {
  state = {
    startDate: new Date(),
    endDate: new Date()
  };

  selectStartDateHandler = (startDate, dayDuration) =>
    this.handleDates({ startDate }, dayDuration);
  selectEndDateHandler = (endDate, dayDuration) =>
    this.handleDates({ endDate }, dayDuration);

  handleDates = ({ startDate, endDate }, dayDuration) => {
    const start = startDate || this.state.startDate;
    let end = endDate || this.state.endDate;

    if (moment(start).isAfter(moment(end))) {
      end = start;
    }

    this.setState({
      startDate: start,
      endDate: end
    });

    return this.createDays(start, end, dayDuration);
  };

  createDays = (start, end, dayDuration) => {
    const numberOfDays = moment(end).diff(moment(start).startOf('day'), 'days');
    const days = [
      {
        date: moment(start).format('YYYY-MM-DD'),
        duration: dayDuration,
        requestStatus: 'PENDING'
      }
    ];

    for (let i = 1; i <= numberOfDays; i++) {
      if (this.isWeekday(moment(start).add(i, 'days'))) {
        days.push({
          date: moment(moment(start).add(i, 'days')).format('YYYY-MM-DD'),
          duration: dayDuration,
          requestStatus: 'PENDING'
        });
      }
    }

    return days;
  };

  isWeekday = input => {
    const dayOfWeek = moment(input).day();
    return dayOfWeek !== 0 && dayOfWeek !== 6;
  };

  setDayLength = (dayLength, days) => {
    return days.map(day => {
      return (day = {
        ...day,
        duration: dayLength
      });
    });
  };

  setAbsenceType = id =>
    this.props.leaveTypes.find(leaveType => leaveType.id === parseInt(id, 16));

  render() {
    let leaveTypes;

    if (this.props.leaveTypes.length > 1) {
      leaveTypes = this.props.leaveTypes.map(leave => {
        return (
          <option key={leave.id} value={leave.id}>
            {leave.name}
          </option>
        );
      });
    }

    return (
      <Formik
        validationSchema={validationSchema}
        initialValues={{
          user: this.props.user,
          absence:
            this.props.leaveTypes.length > 1
              ? {
                  id: ''
                }
              : this.props.leaveTypes[0],
          reason: '',
          requestStatus: 'PENDING',
          days: [
            {
              date: moment().format('YYYY-MM-DD'),
              duration: 'FULL_DAY',
              requestStatus: 'PENDING'
            }
          ],
          currentlyApprovedBy: []
        }}
        onSubmit={values =>
          this.props.dispatch(this.props.requestAction(values))
        }
        render={({
          handleSubmit,
          handleChange,
          values,
          errors,
          touched,
          setFieldValue
          //   TODO: Implement request overview screen
          //   validateForm,
          //   setTouched
        }) => (
          <div className="d-flex justify-content-between">
            <div className="px-2">
              {this.props.leaveTypes.length > 1 && (
                <div className="mb-2">
                  <label htmlFor="leaveType">Type of Leave</label>
                  <select
                    className={`${
                      errors.absence && touched.absence ? 'border-danger' : ''
                    } mb-3 border`}
                    name="absence.id"
                    onChange={e => {
                      setFieldValue(
                        'absence',
                        this.setAbsenceType(e.currentTarget.value)
                      );
                    }}
                    value={values.absence.id}
                  >
                    <option value="" disabled>
                      Select leave type
                    </option>
                    {leaveTypes}
                  </select>
                </div>
              )}
              <div className="mb-2">
                <span>Start Date</span>
                <DatePicker
                  className="border"
                  locale="en-GB"
                  selected={this.state.startDate}
                  selectsStart
                  startDate={this.state.startDate}
                  endDate={this.state.endDate}
                  onChange={e => {
                    setFieldValue(
                      'days',
                      this.selectStartDateHandler(e, values.days[0].duration)
                    );
                  }}
                  filterDate={this.isWeekday}
                />
              </div>
              <div className="mb-2">
                <span>End Date</span>
                <div>
                  <DatePicker
                    className="border"
                    locale="en-GB"
                    selected={this.state.endDate}
                    selectsEnd
                    startDate={this.state.startDate}
                    endDate={this.state.endDate}
                    onChange={e => {
                      setFieldValue(
                        'days',
                        this.selectEndDateHandler(e, values.days[0].duration)
                      );
                    }}
                    filterDate={this.isWeekday}
                  />
                </div>
              </div>
              <div>
                <input
                  onChange={() => {
                    setFieldValue(
                      'days',
                      this.setDayLength('FULL_DAY', values.days)
                    );
                  }}
                  id="radio-1"
                  name="duration"
                  defaultChecked
                  type="radio"
                />
                <label htmlFor="radio-1" className="radio-label">
                  Full day
                </label>
              </div>
              <div>
                <input
                  onChange={() => {
                    setFieldValue(
                      'days',
                      this.setDayLength('MORNING', values.days)
                    );
                  }}
                  id="radio-2"
                  value="MORNING"
                  name="duration"
                  type="radio"
                />
                <label htmlFor="radio-2" className="radio-label">
                  Morning only
                </label>
              </div>
              <div>
                <input
                  onChange={() => {
                    setFieldValue(
                      'days',
                      this.setDayLength('AFTERNOON', values.days)
                    );
                  }}
                  id="radio-3"
                  value="AFTERNOON"
                  name="duration"
                  type="radio"
                />
                <label htmlFor="radio-3" className="radio-label">
                  Afternoon only
                </label>
              </div>
            </div>
            <div className="px-2 d-flex flex-column">
              <textarea
                className={`${
                  errors.reason && touched.reason ? 'border-danger' : ''
                } mb-3 border`}
                name="reason"
                rows="10"
                onChange={handleChange}
                placeholder="Please enter a reason"
              />
              <div className="d-flex justify-content-end">
                {/* 
                TODO: Implement request overview screen
                <button
                  type="button"
                  onClick={() =>
                    validateForm().then(errors => {
                      setTouched(errors);
                    })
                  }
                >
                  Next
                </button> */}

                <button className="btn" onClick={handleSubmit} type="submit">
                  Apply Leave
                </button>
              </div>
            </div>
          </div>
        )}
      />
    );
  }
}

export default connect()(RequestForm);
