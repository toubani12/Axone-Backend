import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResume } from '../resume.model';
import { ResumeService } from '../service/resume.service';

const resumeResolve = (route: ActivatedRouteSnapshot): Observable<null | IResume> => {
  const id = route.params['id'];
  if (id) {
    return inject(ResumeService)
      .find(id)
      .pipe(
        mergeMap((resume: HttpResponse<IResume>) => {
          if (resume.body) {
            return of(resume.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default resumeResolve;
