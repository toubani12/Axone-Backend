import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ITechCVHardSkills } from '../tech-cv-hard-skills.model';
import { TechCVHardSkillsService } from '../service/tech-cv-hard-skills.service';

import techCVHardSkillsResolve from './tech-cv-hard-skills-routing-resolve.service';

describe('TechCVHardSkills routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: TechCVHardSkillsService;
  let resultTechCVHardSkills: ITechCVHardSkills | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(TechCVHardSkillsService);
    resultTechCVHardSkills = undefined;
  });

  describe('resolve', () => {
    it('should return ITechCVHardSkills returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        techCVHardSkillsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTechCVHardSkills = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTechCVHardSkills).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        techCVHardSkillsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTechCVHardSkills = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTechCVHardSkills).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITechCVHardSkills>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        techCVHardSkillsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTechCVHardSkills = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTechCVHardSkills).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
