import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { TechnicalCVService } from '../service/technical-cv.service';
import { ITechnicalCV } from '../technical-cv.model';
import { TechnicalCVFormService } from './technical-cv-form.service';

import { TechnicalCVUpdateComponent } from './technical-cv-update.component';

describe('TechnicalCV Management Update Component', () => {
  let comp: TechnicalCVUpdateComponent;
  let fixture: ComponentFixture<TechnicalCVUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let technicalCVFormService: TechnicalCVFormService;
  let technicalCVService: TechnicalCVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechnicalCVUpdateComponent],
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
      .overrideTemplate(TechnicalCVUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechnicalCVUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    technicalCVFormService = TestBed.inject(TechnicalCVFormService);
    technicalCVService = TestBed.inject(TechnicalCVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const technicalCV: ITechnicalCV = { id: 456 };

      activatedRoute.data = of({ technicalCV });
      comp.ngOnInit();

      expect(comp.technicalCV).toEqual(technicalCV);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechnicalCV>>();
      const technicalCV = { id: 123 };
      jest.spyOn(technicalCVFormService, 'getTechnicalCV').mockReturnValue(technicalCV);
      jest.spyOn(technicalCVService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ technicalCV });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: technicalCV }));
      saveSubject.complete();

      // THEN
      expect(technicalCVFormService.getTechnicalCV).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(technicalCVService.update).toHaveBeenCalledWith(expect.objectContaining(technicalCV));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechnicalCV>>();
      const technicalCV = { id: 123 };
      jest.spyOn(technicalCVFormService, 'getTechnicalCV').mockReturnValue({ id: null });
      jest.spyOn(technicalCVService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ technicalCV: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: technicalCV }));
      saveSubject.complete();

      // THEN
      expect(technicalCVFormService.getTechnicalCV).toHaveBeenCalled();
      expect(technicalCVService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechnicalCV>>();
      const technicalCV = { id: 123 };
      jest.spyOn(technicalCVService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ technicalCV });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(technicalCVService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
