import { Formik } from 'formik';
import React, { Component } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import { createLeaveRequest } from '../../../store/actions/leaveRequestActions';
import { getUser } from '../../../store/selectors';

const validationSchema = Yup.object().shape({
  name: Yup.string().required()
});

class LeaveRequest extends Component {
  state = {
    startDate: new Date()
  };

  handleChange = date => {
    this.setState({
      startDate: date
    });
  };

  render() {
    console.log(this.props);
    return (
      <Formik
        // validationSchema={validationSchema}
        initialValues={{
          user: this.props.user,
          absence: {
            id: 1,
            absenceType: 'DEDUCTED_LEAVE',
            absenceSubtype: 'ANNUAL',
            name: 'Annual leave - Vacation',
            comment: 'string',
            active: true,
            iconUrl: 'icons/vacation.png'
          }
        }}
        onSubmit={this.props.createLeaveRequest}
        render={({
          handleSubmit,
          handleChange,
          values,
          errors,
          touched,
          setFieldValue
        }) => (
          <div className="d-flex justify-content-between">
            {console.log(values)}
            <div className="px-2">
              <div className="d-flex justify-content-between mb-2">
                <label htmlFor="leaveType">Type of Leave</label>
                <select name="leaveType">
                  <option value="test">Annual leave</option>
                </select>
              </div>
              <div className="d-flex justify-content-between mb-2">
                <label htmlFor="startDate">Start date</label>
                <DatePicker
                  selected={this.state.startDate}
                  onChange={this.handleChange}
                  test="test"
                />
              </div>
              {/* <div className="d-flex justify-content-between mb-2">
            <label htmlFor="endDate">End date</label>
            <DatePicker
              selected={this.state.endDate}
              onChange={this.handleChange}
            />
          </div> */}
            </div>
            <div className="px-2 d-flex flex-column">
              <textarea
                className="mb-3"
                name="reason"
                id=""
                rows="10"
                onChange={handleChange}
              />
              <div className="d-flex justify-content-end">
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

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createLeaveRequest }, dispatch);

const mapStateToProps = state => ({
  user: getUser(state)
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LeaveRequest);
