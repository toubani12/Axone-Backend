import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RecruiterService } from 'app/entities/recruiter/service/recruiter.service';
import { IRequest } from '../request.model';
import { RequestService } from '../service/request.service';
import { RequestFormService } from './request-form.service';

import { RequestUpdateComponent } from './request-update.component';

describe('Request Management Update Component', () => {
  let comp: RequestUpdateComponent;
  let fixture: ComponentFixture<RequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let requestFormService: RequestFormService;
  let requestService: RequestService;
  let applicationService: ApplicationService;
  let recruiterService: RecruiterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RequestUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    requestFormService = TestBed.inject(RequestFormService);
    requestService = TestBed.inject(RequestService);
    applicationService = TestBed.inject(ApplicationService);
    recruiterService = TestBed.inject(RecruiterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call relatedApplication query and add missing value', () => {
      const request: IRequest = { id: 456 };
      const relatedApplication: IApplication = { id: 8591 };
      request.relatedApplication = relatedApplication;

      const relatedApplicationCollection: IApplication[] = [{ id: 32767 }];
      jest.spyOn(applicationService, 'query').mockReturnValue(of(new HttpResponse({ body: relatedApplicationCollection })));
      const expectedCollection: IApplication[] = [relatedApplication, ...relatedApplicationCollection];
      jest.spyOn(applicationService, 'addApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ request });
      comp.ngOnInit();

      expect(applicationService.query).toHaveBeenCalled();
      expect(applicationService.addApplicationToCollectionIfMissing).toHaveBeenCalledWith(relatedApplicationCollection, relatedApplication);
      expect(comp.relatedApplicationsCollection).toEqual(expectedCollection);
    });

    it('Should call Recruiter query and add missing value', () => {
      const request: IRequest = { id: 456 };
      const recruiter: IRecruiter = { id: 12312 };
      request.recruiter = recruiter;

      const recruiterCollection: IRecruiter[] = [{ id: 23511 }];
      jest.spyOn(recruiterService, 'query').mockReturnValue(of(new HttpResponse({ body: recruiterCollection })));
      const additionalRecruiters = [recruiter];
      const expectedCollection: IRecruiter[] = [...additionalRecruiters, ...recruiterCollection];
      jest.spyOn(recruiterService, 'addRecruiterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ request });
      comp.ngOnInit();

      expect(recruiterService.query).toHaveBeenCalled();
      expect(recruiterService.addRecruiterToCollectionIfMissing).toHaveBeenCalledWith(
        recruiterCollection,
        ...additionalRecruiters.map(expect.objectContaining),
      );
      expect(comp.recruitersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const request: IRequest = { id: 456 };
      const relatedApplication: IApplication = { id: 15241 };
      request.relatedApplication = relatedApplication;
      const recruiter: IRecruiter = { id: 17977 };
      request.recruiter = recruiter;

      activatedRoute.data = of({ request });
      comp.ngOnInit();

      expect(comp.relatedApplicationsCollection).toContain(relatedApplication);
      expect(comp.recruitersSharedCollection).toContain(recruiter);
      expect(comp.request).toEqual(request);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequest>>();
      const request = { id: 123 };
      jest.spyOn(requestFormService, 'getRequest').mockReturnValue(request);
      jest.spyOn(requestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ request });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: request }));
      saveSubject.complete();

      // THEN
      expect(requestFormService.getRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(requestService.update).toHaveBeenCalledWith(expect.objectContaining(request));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequest>>();
      const request = { id: 123 };
      jest.spyOn(requestFormService, 'getRequest').mockReturnValue({ id: null });
      jest.spyOn(requestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ request: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: request }));
      saveSubject.complete();

      // THEN
      expect(requestFormService.getRequest).toHaveBeenCalled();
      expect(requestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequest>>();
      const request = { id: 123 };
      jest.spyOn(requestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ request });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(requestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApplication', () => {
      it('Should forward to applicationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationService, 'compareApplication');
        comp.compareApplication(entity, entity2);
        expect(applicationService.compareApplication).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRecruiter', () => {
      it('Should forward to recruiterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(recruiterService, 'compareRecruiter');
        comp.compareRecruiter(entity, entity2);
        expect(recruiterService.compareRecruiter).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
