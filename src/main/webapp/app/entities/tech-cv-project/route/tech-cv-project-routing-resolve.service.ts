import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechCVProject } from '../tech-cv-project.model';
import { TechCVProjectService } from '../service/tech-cv-project.service';

const techCVProjectResolve = (route: ActivatedRouteSnapshot): Observable<null | ITechCVProject> => {
  const id = route.params['id'];
  if (id) {
    return inject(TechCVProjectService)
      .find(id)
      .pipe(
        mergeMap((techCVProject: HttpResponse<ITechCVProject>) => {
          if (techCVProject.body) {
            return of(techCVProject.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default techCVProjectResolve;
