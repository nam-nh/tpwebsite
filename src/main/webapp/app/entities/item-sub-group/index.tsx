import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ItemSubGroup from './item-sub-group';
import ItemSubGroupDetail from './item-sub-group-detail';
import ItemSubGroupUpdate from './item-sub-group-update';
import ItemSubGroupDeleteDialog from './item-sub-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ItemSubGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ItemSubGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ItemSubGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={ItemSubGroup} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ItemSubGroupDeleteDialog} />
  </>
);

export default Routes;
