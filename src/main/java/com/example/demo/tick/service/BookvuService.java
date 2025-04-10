package com.example.demo.tick.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.tick.bean.BookticketBean;
import com.example.demo.tick.bean.BookticketvuBean;
import com.example.demo.tick.repo.BooktickRepository;
import com.example.demo.tick.repo.BooktickvuRepository;

@Service
public class BookvuService {

	@Autowired
	private BooktickvuRepository booktickvuRepo;
	@Autowired
	private BooktickRepository booktickRepository;

	public List<BookticketvuBean> tickfindAll() {

		return booktickvuRepo.findAll();
	}

	public Optional<BookticketvuBean> findtickbyid(Integer id) {
		return booktickvuRepo.findById(id);
	}

	public List<BookticketBean> findseatbyhallid(Integer hallid, Integer showtimeid) {
		return booktickRepository.findSeatidbyHallidShowtimeid(hallid, showtimeid);

	}

	public BookticketBean updatetick(BookticketBean bean) {
		return booktickRepository.save(bean);

	}

	public void deltick(Integer id) {
		booktickRepository.deleteById(id);
	}

	///////////////////////////// find other
	public List<BookticketvuBean> findother(Integer se, Integer id) {

		if (se == 1) {
			return booktickvuRepo.findbyTickid(id);
		} else if (se == 3) {
			return booktickvuRepo.findbyUserid(id);

		} else if (se == 5) {

			return booktickvuRepo.findbyHallid(id);
		}
		return null;
	}

	public List<BookticketvuBean> findothers(Integer se, String id) {

		if (se == 2) {

			return booktickvuRepo.findbyOrderid(id);
		} else if (se == 4) {

			return booktickvuRepo.findbyShowtimedate(id);
		} else if (se == 6) {

			return booktickvuRepo.findbyMoviename(id);
		} else if (se == 7) {

			return booktickvuRepo.findbyPayout(id);
		}
		return null;
	}

}
