import { Formik } from 'formik';
import React, { Component } from 'react';
import Switch from 'react-switch';
import * as Yup from 'yup';

const validationSchema = Yup.object().shape({
  name: Yup.string().required()
});

export default class SicknessRequest extends Component {
  render() {
    return (
      <div>
        <Formik
          validationSchema={validationSchema}
          initialValues={{
            name: '',
            teamApprovers: [],
            users: [],
            sendEmailToTeammatesForBonusRequestEnabled: false,
            sendEmailToTeammatesForSicknessRequestEnabled: false,
            sendEmailToTeammatesForAnnualRequestEnabled: false
          }}
          onSubmit={this.props.createTeam}
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
                } w-100 border mb-3`}
                name="name"
                onChange={handleChange}
                placeholder="Team name"
              />
              <input
                className="w-100 border mb-3"
                name="sicknessRequestEmails"
                defaultValue={values.sicknessRequestEmails}
                placeholder="Sickness request emails"
                onChange={handleChange}
              />
              <input
                className="w-100 border mb-3"
                name="annualRequestEmails"
                defaultValue={values.annualRequestEmails}
                placeholder="Annual request emails"
                onChange={handleChange}
              />
              <input
                className="w-100 border mb-3"
                name="bonusRequestEmails"
                defaultValue={values.bonusRequestEmails}
                placeholder="Bonus request emails"
                onChange={handleChange}
              />
              <div className="d-flex justify-content-between mb-3">
                <h5>Send email to teammates for annual request</h5>
                <Switch
                  onChange={() =>
                    setFieldValue(
                      'sendEmailToTeammatesForAnnualRequestEnabled',
                      !values.sendEmailToTeammatesForAnnualRequestEnabled
                    )
                  }
                  checked={values.sendEmailToTeammatesForAnnualRequestEnabled}
                  name="sendEmailToTeammatesForAnnualRequestEnabled"
                />
              </div>
              <div className="d-flex justify-content-between mb-3">
                <h5>Send email to teammates for bonus request</h5>
                <Switch
                  onChange={() =>
                    setFieldValue(
                      'sendEmailToTeammatesForBonusRequestEnabled',
                      !values.sendEmailToTeammatesForBonusRequestEnabled
                    )
                  }
                  checked={values.sendEmailToTeammatesForBonusRequestEnabled}
                  name="sendEmailToTeammatesForBonusRequestEnabled"
                />
              </div>
              <div className="d-flex justify-content-between mb-3">
                <h5>Send email to teammates for bonus request</h5>
                <Switch
                  onChange={() =>
                    setFieldValue(
                      'sendEmailToTeammatesForSicknessRequestEnabled',
                      !values.sendEmailToTeammatesForSicknessRequestEnabled
                    )
                  }
                  checked={values.sendEmailToTeammatesForSicknessRequestEnabled}
                  name="sendEmailToTeammatesForSicknessRequestEnabled"
                />
              </div>
              <button className="btn" onClick={handleSubmit} type="submit">
                Create team
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}
