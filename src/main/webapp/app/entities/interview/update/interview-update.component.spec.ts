import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IInterview } from '../interview.model';
import { InterviewService } from '../service/interview.service';
import { InterviewFormService } from './interview-form.service';

import { InterviewUpdateComponent } from './interview-update.component';

describe('Interview Management Update Component', () => {
  let comp: InterviewUpdateComponent;
  let fixture: ComponentFixture<InterviewUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let interviewFormService: InterviewFormService;
  let interviewService: InterviewService;
  let candidateService: CandidateService;
  let applicationService: ApplicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, InterviewUpdateComponent],
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
      .overrideTemplate(InterviewUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InterviewUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    interviewFormService = TestBed.inject(InterviewFormService);
    interviewService = TestBed.inject(InterviewService);
    candidateService = TestBed.inject(CandidateService);
    applicationService = TestBed.inject(ApplicationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Candidate query and add missing value', () => {
      const interview: IInterview = { id: 456 };
      const attendee: ICandidate = { id: 19970 };
      interview.attendee = attendee;

      const candidateCollection: ICandidate[] = [{ id: 24766 }];
      jest.spyOn(candidateService, 'query').mockReturnValue(of(new HttpResponse({ body: candidateCollection })));
      const additionalCandidates = [attendee];
      const expectedCollection: ICandidate[] = [...additionalCandidates, ...candidateCollection];
      jest.spyOn(candidateService, 'addCandidateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ interview });
      comp.ngOnInit();

      expect(candidateService.query).toHaveBeenCalled();
      expect(candidateService.addCandidateToCollectionIfMissing).toHaveBeenCalledWith(
        candidateCollection,
        ...additionalCandidates.map(expect.objectContaining),
      );
      expect(comp.candidatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Application query and add missing value', () => {
      const interview: IInterview = { id: 456 };
      const application: IApplication = { id: 9756 };
      interview.application = application;

      const applicationCollection: IApplication[] = [{ id: 27499 }];
      jest.spyOn(applicationService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationCollection })));
      const additionalApplications = [application];
      const expectedCollection: IApplication[] = [...additionalApplications, ...applicationCollection];
      jest.spyOn(applicationService, 'addApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ interview });
      comp.ngOnInit();

      expect(applicationService.query).toHaveBeenCalled();
      expect(applicationService.addApplicationToCollectionIfMissing).toHaveBeenCalledWith(
        applicationCollection,
        ...additionalApplications.map(expect.objectContaining),
      );
      expect(comp.applicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const interview: IInterview = { id: 456 };
      const attendee: ICandidate = { id: 17936 };
      interview.attendee = attendee;
      const application: IApplication = { id: 10516 };
      interview.application = application;

      activatedRoute.data = of({ interview });
      comp.ngOnInit();

      expect(comp.candidatesSharedCollection).toContain(attendee);
      expect(comp.applicationsSharedCollection).toContain(application);
      expect(comp.interview).toEqual(interview);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInterview>>();
      const interview = { id: 123 };
      jest.spyOn(interviewFormService, 'getInterview').mockReturnValue(interview);
      jest.spyOn(interviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: interview }));
      saveSubject.complete();

      // THEN
      expect(interviewFormService.getInterview).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(interviewService.update).toHaveBeenCalledWith(expect.objectContaining(interview));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInterview>>();
      const interview = { id: 123 };
      jest.spyOn(interviewFormService, 'getInterview').mockReturnValue({ id: null });
      jest.spyOn(interviewService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interview: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: interview }));
      saveSubject.complete();

      // THEN
      expect(interviewFormService.getInterview).toHaveBeenCalled();
      expect(interviewService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInterview>>();
      const interview = { id: 123 };
      jest.spyOn(interviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(interviewService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCandidate', () => {
      it('Should forward to candidateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(candidateService, 'compareCandidate');
        comp.compareCandidate(entity, entity2);
        expect(candidateService.compareCandidate).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareApplication', () => {
      it('Should forward to applicationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationService, 'compareApplication');
        comp.compareApplication(entity, entity2);
        expect(applicationService.compareApplication).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
