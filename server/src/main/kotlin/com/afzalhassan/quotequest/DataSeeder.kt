package com.afzalhassan.quotequest

import com.afzalhassan.quotequest.quote.Quote
import com.afzalhassan.quotequest.quote.QuoteRepository
import com.afzalhassan.quotequest.user.User
import com.afzalhassan.quotequest.user.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataSeeder(
    private val quoteRepository: QuoteRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        if (quoteRepository.count() == 0L) {
            quoteRepository.saveAll(quotes)
        }
        if (userRepository.count() == 0L) {
            userRepository.saveAll(
                listOf(
                    User("afzal@mail.com", passwordEncoder.encode("135@Afzal")!!, "Afzal"),
                    User("dimitar@mail.com", passwordEncoder.encode("579@Dimitar")!!, "Dimitar"),
                )
            )
        }
    }

    private val quotes = listOf(
        Quote("The only thing we have to fear is fear itself.", "Franklin D. Roosevelt"),
        Quote("I think, therefore I am.", "René Descartes"),
        Quote("The unexamined life is not worth living.", "Socrates"),
        Quote("Imagination is more important than knowledge.", "Albert Einstein"),
        Quote("Life is like riding a bicycle. To keep your balance, you must keep moving.", "Albert Einstein"),
        Quote("Ask not what your country can do for you - ask what you can do for your country.", "John F. Kennedy"),
        Quote("Darkness cannot drive out darkness; only light can do that.", "Martin Luther King Jr."),
        Quote("That's one small step for man, one giant leap for mankind.", "Neil Armstrong"),
        Quote("To be, or not to be, that is the question.", "William Shakespeare"),
        Quote("All the world's a stage, and all the men and women merely players.", "William Shakespeare"),
        Quote("Your time is limited, so don't waste it living someone else's life.", "Steve Jobs"),
        Quote("Education is the most powerful weapon which you can use to change the world.", "Nelson Mandela"),
        Quote("I came, I saw, I conquered.", "Julius Caesar"),
        Quote("We shall fight on the beaches.", "Winston Churchill"),
        Quote("I have nothing to offer but blood, toil, tears and sweat.", "Winston Churchill"),
        Quote("I can resist everything except temptation.", "Oscar Wilde"),
        Quote("The journey of a thousand miles begins with one step.", "Lao Tzu"),
        Quote("Float like a butterfly, sting like a bee.", "Muhammad Ali"),
        Quote("If I have seen further, it is by standing on the shoulders of giants.", "Isaac Newton"),
        Quote("Nothing in life is to be feared, it is only to be understood.", "Marie Curie"),
    )
}