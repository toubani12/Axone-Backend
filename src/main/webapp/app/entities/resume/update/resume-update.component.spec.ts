import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { ResumeService } from '../service/resume.service';
import { IResume } from '../resume.model';
import { ResumeFormService } from './resume-form.service';

import { ResumeUpdateComponent } from './resume-update.component';

describe('Resume Management Update Component', () => {
  let comp: ResumeUpdateComponent;
  let fixture: ComponentFixture<ResumeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resumeFormService: ResumeFormService;
  let resumeService: ResumeService;
  let candidateService: CandidateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ResumeUpdateComponent],
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
      .overrideTemplate(ResumeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResumeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resumeFormService = TestBed.inject(ResumeFormService);
    resumeService = TestBed.inject(ResumeService);
    candidateService = TestBed.inject(CandidateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Candidate query and add missing value', () => {
      const resume: IResume = { id: 456 };
      const owner: ICandidate = { id: 5352 };
      resume.owner = owner;

      const candidateCollection: ICandidate[] = [{ id: 26920 }];
      jest.spyOn(candidateService, 'query').mockReturnValue(of(new HttpResponse({ body: candidateCollection })));
      const additionalCandidates = [owner];
      const expectedCollection: ICandidate[] = [...additionalCandidates, ...candidateCollection];
      jest.spyOn(candidateService, 'addCandidateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resume });
      comp.ngOnInit();

      expect(candidateService.query).toHaveBeenCalled();
      expect(candidateService.addCandidateToCollectionIfMissing).toHaveBeenCalledWith(
        candidateCollection,
        ...additionalCandidates.map(expect.objectContaining),
      );
      expect(comp.candidatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resume: IResume = { id: 456 };
      const owner: ICandidate = { id: 10681 };
      resume.owner = owner;

      activatedRoute.data = of({ resume });
      comp.ngOnInit();

      expect(comp.candidatesSharedCollection).toContain(owner);
      expect(comp.resume).toEqual(resume);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResume>>();
      const resume = { id: 123 };
      jest.spyOn(resumeFormService, 'getResume').mockReturnValue(resume);
      jest.spyOn(resumeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resume });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resume }));
      saveSubject.complete();

      // THEN
      expect(resumeFormService.getResume).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(resumeService.update).toHaveBeenCalledWith(expect.objectContaining(resume));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResume>>();
      const resume = { id: 123 };
      jest.spyOn(resumeFormService, 'getResume').mockReturnValue({ id: null });
      jest.spyOn(resumeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resume: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resume }));
      saveSubject.complete();

      // THEN
      expect(resumeFormService.getResume).toHaveBeenCalled();
      expect(resumeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResume>>();
      const resume = { id: 123 };
      jest.spyOn(resumeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resume });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resumeService.update).toHaveBeenCalled();
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
  });
});
