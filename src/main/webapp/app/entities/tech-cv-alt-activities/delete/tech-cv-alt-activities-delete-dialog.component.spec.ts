jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TechCVAltActivitiesService } from '../service/tech-cv-alt-activities.service';

import { TechCVAltActivitiesDeleteDialogComponent } from './tech-cv-alt-activities-delete-dialog.component';

describe('TechCVAltActivities Management Delete Component', () => {
  let comp: TechCVAltActivitiesDeleteDialogComponent;
  let fixture: ComponentFixture<TechCVAltActivitiesDeleteDialogComponent>;
  let service: TechCVAltActivitiesService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TechCVAltActivitiesDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(TechCVAltActivitiesDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TechCVAltActivitiesDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TechCVAltActivitiesService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
