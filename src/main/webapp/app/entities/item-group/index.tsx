import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ItemGroup from './item-group';
import ItemGroupDetail from './item-group-detail';
import ItemGroupUpdate from './item-group-update';
import ItemGroupDeleteDialog from './item-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ItemGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ItemGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ItemGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={ItemGroup} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ItemGroupDeleteDialog} />
  </>
);

export default Routes;
