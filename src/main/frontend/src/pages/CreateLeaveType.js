import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Switch from 'react-switch';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import {
  createLeaveType,
  requestLeaveType
} from '../store/actions/leaveTypeActions';
import { getLeaveType } from '../store/selectors';

const validationSchema = Yup.object().shape({
  name: Yup.string().required()
});

class CreateLeaveType extends Component {
  render() {
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={{
            absenceType: 'NON_DEDUCTED_LEAVE',
            name: '',
            comment: '',
            active: false
          }}
          onSubmit={this.props.createLeaveType}
          enableReinitialize
          render={({
            handleSubmit,
            handleChange,
            values,
            errors,
            touched,
            setFieldValue
          }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.name && touched.name ? 'border-danger' : ''
                } w-100 mb-3 border`}
                name="name"
                defaultValue={values.name}
                placeholder="Team name"
                onChange={handleChange}
              />
              <input
                className="w-100 border mb-3"
                name="comment"
                defaultValue={values.comment}
                placeholder="Comment"
                onChange={handleChange}
              />
              <div className="d-flex justify-content-between mb-3">
                <h5>Active</h5>
                <Switch
                  onChange={() => setFieldValue('active', !values.active)}
                  checked={values.active}
                  name="active"
                />
              </div>
              <button className="btn" onClick={handleSubmit} type="submit">
                Create leave type
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  leaveType: getLeaveType(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestLeaveType, createLeaveType }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(CreateLeaveType));
